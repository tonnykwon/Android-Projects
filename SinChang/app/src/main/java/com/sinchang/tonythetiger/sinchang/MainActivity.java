package com.sinchang.tonythetiger.sinchang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private String strUrl = "http://54.249.17.93/show_content.php";
    private String inoutUrl = "http://54.249.17.93/inout_content.php";
    ListView listView;
    TextListAdapter adapter;
    checkTable ctable;
    RadioButton inBtn;
    RadioButton outBtn;
    EditText boxnumber;

    boolean barcodeState;

    //넘기는 변수
    String boxn;
    String sr;
    String date;
    String code;
    String name;
    String setclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("재고 현황");

        //null 초기화
        sr=null;
        date=null;
        code=null;
        name=null;

        //리스트 초기화 및 설정
        listView = (ListView) findViewById(R.id.listView);
        adapter = new TextListAdapter(getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inoutDialog(position);
            }
        });



        //spinner 설정
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinnerdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.product_class, R.layout.spinner_item);
        spinnerdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spinnerdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setclass = "%";
                } else if (position == 1) {
                    setclass = "과자";
                } else if (position == 2) {
                    setclass = "음료";
                } else if (position == 3) {
                    setclass = "주류";
                } else if (position == 4) {
                    setclass = "빙과";
                } else if (position == 5) {
                    setclass = "담배";
                } else if (position == 6) {
                    setclass = "잡화";
                } else if (position == 7) {
                    setclass = "기타";
                }
                adapter.clear();
                checkTable check = new checkTable();
                check.execute();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ctable = new checkTable();
        ctable.execute();

        barcodeState = true;
        barcodeScan();
    }

    //inoutDialog에서 정보 가져오기
    public void inOutEdit(){
        boxn = boxnumber.getText().toString();
        if(!boxn.isEmpty()){
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            date = (new SimpleDateFormat("yyyyMMddHHmmss").format(today));
            InOutContent inOutContent = new InOutContent();
            inOutContent.execute();
        } else {
            Toast.makeText(getApplicationContext(), "박스 수량을 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class checkTable extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

        StringBuilder builder = new StringBuilder();
        JSONObject jObject;
        JSONArray jArray = null;
        ArrayList<HashMap<String, String>> productlist = new ArrayList<HashMap<String, String>>();
        ProgressDialog progressBar;

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {

            //정보 받아오기
            try{
                URL url = new URL(strUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if(conn!=null){
                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        StringBuffer buffer = new StringBuffer();
                        buffer.append("setclass").append("=").append(setclass);

                        OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream());
                        PrintWriter writer=  new PrintWriter(outStream);
                        writer.write(buffer.toString());
                        writer.flush();
                        writer.close();

                    int resCode = conn.getResponseCode();
                    if(resCode==HttpURLConnection.HTTP_OK){
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        String line =null;
                        while(true){
                            line = reader.readLine();
                            if(line==null){
                                break;
                            }
                            builder.append(line+"\n");
                        }
                        reader.close();
                        conn.disconnect();
                    }
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            Log.d("Server", "output - " + builder.toString());
            String json = builder.toString();

            //JSON 형식으로 변환
            try{
                jObject = new JSONObject(json);
                jArray=jObject.getJSONArray("products");
                for(int i=0; i<jArray.length(); i++){
                    JSONObject c = jArray.getJSONObject(i);
                    String name= c.getString("name");
                    String boxn = c.getString("boxn");
                    String pinboxn= c.getString("pinboxn");
                    String wholen = c.getString("wholen");
                    String code = c.getString("code");
                    String barcode = c.getString("barcode");
                    HashMap<String, String> product = new HashMap<String, String>();
                    product.put("name", name);
                    product.put("boxn", boxn);
                    product.put("pinboxn", pinboxn);
                    product.put("wholen", wholen);
                    product.put("code", code);
                    product.put("barcode", barcode);

                    productlist.add(product);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }

            return productlist;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(MainActivity.this);
            progressBar.setIndeterminate(false);
            progressBar.setCancelable(true);
            progressBar.setMessage("로딩중...");
            progressBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> list) {

            progressBar.dismiss();
            if(list==null){
                Toast.makeText(getApplicationContext(), "결과없음", Toast.LENGTH_SHORT).show();
            } else{
                for(int i =0; i<list.size(); i++){
                    HashMap<String, String> a;
                    a = productlist.get(i);
                    String name = a.get("name");
                    String boxn = a.get("boxn");
                    String pinboxn = a.get("pinboxn");
                    String wholen = a.get("wholen");
                    String code = a.get("code");
                    String barcode = a.get("barcode");
                    adapter.addItem(new TextItem(name, boxn, pinboxn, wholen, code, barcode));
                }
            }
            listView.setAdapter(adapter);
        }
    }

    class InOutContent extends AsyncTask<String, String ,String>{
        @Override
        protected String doInBackground(String... params) {

            StringBuilder builder = new StringBuilder();

            try{
                URL url = new URL(inoutUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn!=null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("code").append("=").append(code).append("&");
                    buffer.append("boxn").append("=").append(boxn).append("&");
                    buffer.append("sr").append("=").append(sr).append("&");
                    buffer.append("date").append("=").append(date).append("&");
                    buffer.append("name").append("=").append(name);

                    OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream());
                    PrintWriter writer=  new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();
                    writer.close();

                    InputStreamReader inStream = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inStream);
                    String str = null;
                    while((str=reader.readLine())!=null){
                        builder.append(str+"\n");
                    }
                    reader.close();
                    conn.disconnect();

                }
            }catch(Exception ex){

            }

            String output = builder.toString();

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            adapter.clear();
            checkTable cTable = new checkTable();
            cTable.execute();
            adapter.notifyDataSetInvalidated();
        }
    }

    public void barcodeScan() {
        if(barcodeState){
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_barcode, null);
            final AlertDialog.Builder buider = new AlertDialog.Builder(MainActivity.this);
            buider.setTitle("바코드 입력");

            buider.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            final EditText barcodeEdit = (EditText) dialogView.findViewById(R.id.barcode);
            buider.setView(dialogView);

            AlertDialog dialog = buider.create();
            dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
            dialog.show();

            barcodeEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(barcodeEdit.getText().length()>8){
                        String barcode = barcodeEdit.getText().toString();
                        int i = 0;
                        for(i =0; i<adapter.getCount(); i++){
                            TextItem item = (TextItem)adapter.getItem(i);
                            String stringBarcode = item.getData(5);
                            if(barcode.equals(stringBarcode)){
                                Toast.makeText(MainActivity.this, "찾았다! - "+ stringBarcode+", "+i, Toast.LENGTH_SHORT).show();
                                barcodeEdit.setText("");
                                inoutDialog(i);
                            }
                        }
                    }
                }
            });
        }
    }

    public void inoutDialog(int position){
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_inout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("입출고 입력");

        //dialogView 참조
        inBtn = (RadioButton) dialogView.findViewById(R.id.inRadio);
        outBtn = (RadioButton) dialogView.findViewById(R.id.outRadio);
        boxnumber = (EditText) dialogView.findViewById(R.id.boxNumber);
        TextView idText = (TextView) dialogView.findViewById(R.id.pId);

        TextItem textitem = (TextItem) adapter.getItem(position);
        code = textitem.getData(4);
        name = textitem.getData(0);
        idText.setText(name);

        builder.setNegativeButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (inBtn.isChecked()){
                    sr="입고";
                    inOutEdit();
                    Toast.makeText(getApplicationContext(), sr+", "+code+", "+name+", "+boxn+", "+date+" 입력 완료", Toast.LENGTH_SHORT).show();
                } else if(outBtn.isChecked()){
                    sr="출고";
                    inOutEdit();
                    Toast.makeText(getApplicationContext(), sr+" 입력 완료", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "입고, 출고 버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setView(dialogView);


        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
        dialog.show();

        }

    public void onBarcodeClick(View view){
        barcodeScan();
    }
}

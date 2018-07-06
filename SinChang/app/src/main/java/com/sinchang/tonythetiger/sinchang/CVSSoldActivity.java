package com.sinchang.tonythetiger.sinchang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinchang.tonythetiger.sinchang.CVSSoldList.ListCVSSoldAdapter;
import com.sinchang.tonythetiger.sinchang.CVSSoldList.SoldItem;
import com.sinchang.tonythetiger.sinchang.InOutList.InOutItem;
import com.sinchang.tonythetiger.sinchang.InOutList.ListInOutAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CVSSoldActivity extends AppCompatActivity {

    ListView listview;
    ListCVSSoldAdapter adapter;
    private String strUrl = "http://54.249.17.93/cvs_sold_content.php";
    private String updateUrl = "http://54.249.17.93/update_cvs_sold_content.php";


    //dt 데이터
    int year;
    int month;
    int day;
    String monthString;
    String dayString;
    String yearString;
    String dt;

    //판매현황 수정
    TextView pName;
    EditText wholeNum;
    SoldItem soldItem;
    String soldId;
    String name;
    String editedWholeNum;
    String delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvssold);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = (ListView) findViewById(R.id.listView);
        adapter = new ListCVSSoldAdapter(getApplicationContext());
        listview.setAdapter(adapter);

        //편의점 날짜별 판매량 수정, 삭제
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_cvs_sold_edit, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(CVSSoldActivity.this);
                builder.setTitle("편의점 판매량 수정");

                soldItem = (SoldItem) adapter.getItem(position);
                soldId = soldItem.getData(5);
                name = soldItem.getData(1);

                //텍스트, 에딕텍스트 참조
                pName = (TextView) dialogView.findViewById(R.id.pName);
                wholeNum = (EditText) dialogView.findViewById(R.id.wholeNum);
                pName.setText(name);

                builder.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editedWholeNum = wholeNum.getText().toString();
                        delete = "0";
                        EditCVSSold editCVSSold = new EditCVSSold();
                        editCVSSold.execute();
                    }
                });

                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setNeutralButton("제거", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete = "1";
                        EditCVSSold editCVSSold = new EditCVSSold();
                        editCVSSold.execute();
                    }
                });

                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();
                return false;
            }
        });

        getDate();
        CVSSoldCheck cvsSoldCheck = new CVSSoldCheck();
        cvsSoldCheck.execute();

    }

    //편의점 판매량 가져오는 스레드
    class CVSSoldCheck extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

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

                    Log.d("Server", "server connection on");

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("dt").append("=").append(dt);

                    OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream());
                    PrintWriter writer = new PrintWriter(outStream);
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
                    String id= c.getString("id");
                    String code = c.getString("code");
                    String name= c.getString("name");
                    String clas = c.getString("class");
                    String wholen = c.getString("wholen");
                    String sold = c.getString("sold");
                    HashMap<String, String> product = new HashMap<String, String>();
                    product.put("id", id);
                    product.put("code", code);
                    product.put("name", name);
                    product.put("clas", clas);
                    product.put("wholen", wholen);
                    product.put("sold", sold);

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
            progressBar = new ProgressDialog(CVSSoldActivity.this);
            progressBar.setIndeterminate(false);
            progressBar.setCancelable(true);
            progressBar.setMessage("로딩중...");
            progressBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> list) {

            progressBar.dismiss();
            if(productlist==null){
                Toast.makeText(getApplicationContext(), "결과없음", Toast.LENGTH_SHORT).show();
            } else{
                for(int i =0; i<list.size(); i++){
                    HashMap<String, String> a;
                    a = productlist.get(i);
                    String id = a.get("id");
                    String code = a.get("code");
                    String name = a.get("name");
                    String wholen = a.get("wholen");
                    String sold = a.get("sold");
                    String clas = a.get("clas");

                    String soldform = String.format("%,d", Integer.parseInt(sold));
                    adapter.addItem(new SoldItem(code, name, wholen, soldform+"원", clas, id));
                }
            }
            listview.setAdapter(adapter);
        }
    }

    class EditCVSSold extends AsyncTask<String, String, String> {

        StringBuilder builder = new StringBuilder();

        @Override
        protected String doInBackground(String... params) {

            //http 연결
            try {
                URL url = new URL(updateUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //정보 받아서 보내기
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("id").append("=").append(soldId).append("&");
                    buffer.append("wholen").append("=").append(wholeNum).append("&");
                    buffer.append("delete").append("=").append(delete);

                    OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream());
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();
                    writer.close();

                    InputStreamReader inStream = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(inStream);
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        builder.append(str + "\n");
                    }
                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String output = builder.toString();
            Log.d("Server", "output - "+output);

            return output;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            adapter.clear();
            CVSSoldCheck cvsSoldCheck = new CVSSoldCheck();
            cvsSoldCheck.execute();
            adapter.notifyDataSetChanged();
        }

    }


    //날짜 버튼 눌렀을시
    public void onDateSetBtn(View view){
        getDate();
        Toast.makeText(getApplicationContext(), dt, Toast.LENGTH_SHORT).show();

        adapter.clear();
        CVSSoldCheck cvsSoldCheck = new CVSSoldCheck();
        cvsSoldCheck.execute();
        adapter.notifyDataSetChanged();
    }

    public void getDate(){
        DatePicker datePicker = (DatePicker) findViewById(R.id.dp);
        year = datePicker.getYear();
        yearString = Integer.toString(year);

        month = datePicker.getMonth()+1;
        if(month<=9){
            monthString = "0"+Integer.toString(month);
        } else{
            monthString = Integer.toString(month);
        }

        day = datePicker.getDayOfMonth();
        if(day<=9){
            dayString = "0"+Integer.toString(day);
        }else{
            dayString = Integer.toString(day);
        }

        dt = yearString+"-"+monthString+"-"+dayString;
    }

}

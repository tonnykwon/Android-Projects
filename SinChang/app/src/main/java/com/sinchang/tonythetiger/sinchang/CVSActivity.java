package com.sinchang.tonythetiger.sinchang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinchang.tonythetiger.sinchang.CVSList.CVSItem;
import com.sinchang.tonythetiger.sinchang.CVSList.ListCVSAdapter;

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

public class CVSActivity extends AppCompatActivity {

    private String strUrl = "http://54.249.17.93/show_cvs_content.php";
    private String soldUrl = "http://54.249.17.93/put_cvs_sold_content.php";
    private String updateUrl = "http://54.249.17.93/update_cvs_content.php";
    ListView listView;
    ListCVSAdapter adapter;

    //cvs sold edt
    EditText wholeNum;
    CVSItem cvsItem;
    String wholeN;
    String dt;
    String code;

    //cvs whole edit
    String wholeCVS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvs);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListCVSAdapter(getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_cvs_sold, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(CVSActivity.this);
                builder.setTitle("판매량");

                TextView pName = (TextView) dialogView.findViewById(R.id.pName);
                wholeNum = (EditText) dialogView.findViewById(R.id.wholeNum);
                cvsItem = (CVSItem) adapter.getItem(position);
                pName.setText(cvsItem.getData(0));

                builder.setNegativeButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar calendar = Calendar.getInstance();
                        Date today = calendar.getTime();

                        dt = (new SimpleDateFormat("yyyy-MM-dd").format(today));
                        wholeN = wholeNum.getText().toString();
                        code = cvsItem.getData(3);

                        CVSSoldPut cvsSoldPut = new CVSSoldPut();
                        cvsSoldPut.execute();
                    }
                });
                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                //dialog 생성
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();
            }
        });

        //전체 수량 변경
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                LayoutInflater inflater = getLayoutInflater();
//                final View dialogView = inflater.inflate(R.layout.dialog_cvs_number, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(CVSActivity.this);
//                builder.setTitle("전체 수량 변경");
//
//                TextView pName = (TextView) dialogView.findViewById(R.id.pName);
//                wholeNum = (EditText) dialogView.findViewById(R.id.wholeNum);
//                cvsItem = (CVSItem) adapter.getItem(position);
//                pName.setText(cvsItem.getData(0));
//
//                builder.setNegativeButton("입력", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        wholeCVS = wholeNum.getText().toString();
//                        code = cvsItem.getData(3);
//
//                        CVSSoldPut cvsSoldPut = new CVSSoldPut();
//                        cvsSoldPut.execute();
//                    }
//                });
//                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                //dialog 생성
//                builder.setView(dialogView);
//                AlertDialog dialog = builder.create();
//                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
//                dialog.show();
//
//                return false;
//            }
//        });

        CheckCVSList checkCVSList = new CheckCVSList();
        checkCVSList.execute();
    }

    class CheckCVSList extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

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
                    String code = c.getString("code");
                    String name = c.getString("name");
                    String wholen = c.getString("wholen");
                    String wholesale = c.getString("wholesale");
                    HashMap<String, String> product = new HashMap<String, String>();
                    product.put("code", code);
                    product.put("name", name);
                    product.put("wholen", wholen);
                    product.put("wholesale", wholesale);

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
            progressBar = new ProgressDialog(CVSActivity.this);
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
                    String code = a.get("code");
                    String name = a.get("name");
                    String wholen = a.get("wholen");
                    String wholesale = a.get("wholesale");
                    adapter.addItem(new CVSItem(name, wholen, wholesale, code));
                }
            }
            listView.setAdapter(adapter);
        }
    }


    //판매수량 입력
    class CVSSoldPut extends AsyncTask<String, String ,String>{
        @Override
        protected String doInBackground(String... params) {

            StringBuilder builder = new StringBuilder();

            try{
                URL url = new URL(soldUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn!=null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("dt").append("=").append(dt).append("&");
                    buffer.append("code").append("=").append(code).append("&");
                    buffer.append("wholen").append("=").append(wholeN);

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
            CheckCVSList cTable = new CheckCVSList();
            cTable.execute();
            adapter.notifyDataSetInvalidated();
        }
    }

    //전체 수량 변경
    class CVSWholeUpdate extends AsyncTask<String, String ,String>{
        @Override
        protected String doInBackground(String... params) {

            StringBuilder builder = new StringBuilder();

            try{
                URL url = new URL(soldUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn!=null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("code").append("=").append(code).append("&");
                    buffer.append("wholen").append("=").append(wholeCVS);

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
            CheckCVSList cTable = new CheckCVSList();
            cTable.execute();
            adapter.notifyDataSetInvalidated();
        }
    }
}

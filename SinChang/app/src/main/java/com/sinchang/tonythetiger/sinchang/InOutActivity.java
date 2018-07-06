package com.sinchang.tonythetiger.sinchang;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InOutActivity extends AppCompatActivity {

    ListView listview;
    ListInOutAdapter adapter;
    private String strUrl = "http://54.249.17.93/inout_show_content.php";
    private String deleteUrl = "http://54.249.17.93/delete_inout.php";

    int year;
    int month;
    int day;
    String monthString;
    String dayString;
    String yearString;

    String dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out);

        //리스트 설정
        listview = (ListView) findViewById(R.id.listView);
        adapter = new ListInOutAdapter(getApplicationContext());
        listview.setAdapter(adapter);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(InOutActivity.this);
                InOutItem inoutItem = (InOutItem) adapter.getItem(position);
                dt = inoutItem.getData(3);
                Toast.makeText(getApplicationContext(), dt, Toast.LENGTH_LONG).show();
                dialog.setTitle("삭제");
                dialog.setMessage("삭제하시겠습니까?");
                dialog.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteInOutContent deleteContent = new DeleteInOutContent();
                        deleteContent.execute();
                    }
                });
                dialog.show();
                return false;
            }
        });

        adapter.clear();
        getDate();
        InOutCheckTable inOutCheckTable = new InOutCheckTable();
        inOutCheckTable.execute();

    }

    public void onDateSetBtn(View view){
        adapter.clear();
        getDate();
        Toast.makeText(getApplicationContext(), yearString+"-"+monthString+"-"+dayString, Toast.LENGTH_SHORT).show();
        InOutCheckTable inOutCheckTable = new InOutCheckTable();
        inOutCheckTable.execute();

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
    }

    //입출고 가져오는 스레드
    class InOutCheckTable extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

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
                    buffer.append("year").append("=").append(yearString).append("&");
                    buffer.append("month").append("=").append(monthString).append("&");
                    buffer.append("day").append("=").append(dayString);

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
                    String dt= c.getString("dt");
                    String code = c.getString("code");
                    String name= c.getString("name");
                    String boxn = c.getString("boxn");
                    String sr = c.getString("sr");
                    HashMap<String, String> product = new HashMap<String, String>();
                    product.put("dt", dt);
                    product.put("code", code);
                    product.put("name", name);
                    product.put("boxn", boxn);
                    product.put("sr", sr);

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
            progressBar = new ProgressDialog(InOutActivity.this);
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
                    String dt = a.get("dt");
                    String code = a.get("code");
                    String name = a.get("name");
                    String boxn = a.get("boxn");
                    String sr = a.get("sr");
                    adapter.addItem(new InOutItem(sr, name, boxn, dt, code));
                }
            }
            listview.setAdapter(adapter);
        }
    }


    //입출고 상황 제거하는 스레드
    class DeleteInOutContent extends AsyncTask<String, String ,String>{
        @Override
        protected String doInBackground(String... params) {

            StringBuilder builder = new StringBuilder();

            try{
                URL url = new URL(deleteUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(conn!=null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("dt").append("=").append(dt);

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

            //새로고침
            adapter.clear();
            getDate();
            InOutCheckTable cTable = new InOutCheckTable();
            cTable.execute();
            adapter.notifyDataSetInvalidated();
        }
    }
}

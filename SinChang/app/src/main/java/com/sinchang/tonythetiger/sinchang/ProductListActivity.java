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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sinchang.tonythetiger.sinchang.ProductList.ListProductAdapter;
import com.sinchang.tonythetiger.sinchang.ProductList.ProductListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductListActivity extends AppCompatActivity {

    private String strUrl = "http://54.249.17.93/show_list_content.php";
    private String updateUrl = "http://54.249.17.93/update_product_list.php";
    private String deleteUrl = "http://54.249.17.93/delete_product_list.php";
    ListView listView;
    ListProductAdapter adapter;
    ProductListItem listItem;

    //dialog EditText
    EditText pCode;
    EditText pClass;
    EditText pName;
    EditText pPinboxn;
    EditText pCost;
    EditText pPrice;
    EditText pBarcode;

    //update date
    String upOriginCode;
    String upCode;
    String upClass;
    String upName;
    String upPinboxn;
    String upCost;
    String upPrice;
    String upBarcode;

    //delete date
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_product_list, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
                builder.setTitle("물품 리스트 추가");

                pCode = (EditText) dialogView.findViewById(R.id.pCode);
                pClass = (EditText) dialogView.findViewById(R.id.pClass);
                pName = (EditText) dialogView.findViewById(R.id.pName);
                pPinboxn = (EditText) dialogView.findViewById(R.id.pPinboxn);
                pCost = (EditText) dialogView.findViewById(R.id.pCost);
                pPrice = (EditText) dialogView.findViewById(R.id.pPrice);
                pBarcode = (EditText) dialogView.findViewById(R.id.pBarcode);



                builder.setNegativeButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getListItemData();
                        upOriginCode = "0";
                        if(upCode==null || upClass==null || upName==null || upPinboxn==null || upCost==null || upPrice==null || upBarcode==null){
                            Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                        } else{
                            Log.d("Server", upOriginCode+upCode+upClass);
                            EditProductList editProductList = new EditProductList();
                            editProductList.execute();
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
        });

        //리스트 초기화 및 설정
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListProductAdapter(getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_product_list, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
                builder.setTitle("물품 정보 수정");

                listItem = (ProductListItem) adapter.getItem(position);

                //참조
                pCode = (EditText) dialogView.findViewById(R.id.pCode);
                pClass = (EditText) dialogView.findViewById(R.id.pClass);
                pName = (EditText) dialogView.findViewById(R.id.pName);
                pPinboxn = (EditText) dialogView.findViewById(R.id.pPinboxn);
                pCost = (EditText) dialogView.findViewById(R.id.pCost);
                pPrice = (EditText) dialogView.findViewById(R.id.pPrice);
                pBarcode = (EditText) dialogView.findViewById(R.id.pBarcode);

                //EditText 기본 설정
                pCode.setText(listItem.getData(0));
                pClass.setText(listItem.getData(1));
                pName.setText(listItem.getData(2));
                pPinboxn.setText(listItem.getData(3));
                pCost.setText(listItem.getData(5));
                pPrice.setText(listItem.getData(6));
                pBarcode.setText(listItem.getData(7));

                builder.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getListItemData();
                        upOriginCode = listItem.getData(0);
                        if (upCode == null || upClass == null || upName == null || upPinboxn == null || upCost == null || upPrice == null) {
                            Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Server", upOriginCode + upCode + upClass);
                            EditProductList editProductList = new EditProductList();
                            editProductList.execute();
                        }
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

                        code = listItem.getData(0);
                        DeleteProductList deleteProductList = new DeleteProductList();
                        deleteProductList.execute();
                    }
                });

                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                return false;
            }
        });

        CheckProductList checkProductList = new CheckProductList();
        checkProductList.execute();
    }

    //물품 리스트 조회
    class CheckProductList extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

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
                    String code= c.getString("code");
                    String clas = c.getString("class");
                    String name= c.getString("name");
                    String pinboxn = c.getString("pinboxn");
                    String wholen = c.getString("wholen");
                    String cost = c.getString("cost");
                    String price = c.getString("price");
                    String barcode = c.getString("barcode");
                    HashMap<String, String> product = new HashMap<String, String>();
                    product.put("code", code);
                    product.put("clas", clas);
                    product.put("name", name);
                    product.put("pinboxn", pinboxn);
                    product.put("wholen", wholen);
                    product.put("cost", cost);
                    product.put("price", price);
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
            progressBar = new ProgressDialog(ProductListActivity.this);
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
                    String clas = a.get("clas");
                    String name = a.get("name");
                    String pinboxn = a.get("pinboxn");
                    String wholen = a.get("wholen");
                    String cost = a.get("cost");
                    String price = a.get("price");
                    String barcode = a.get("barcode");
                    adapter.addItem(new ProductListItem(code, clas, name, pinboxn, wholen, cost, price, barcode));
                }
            }
            listView.setAdapter(adapter);
        }
    }

    //ProductList 수정 혹은 추가
    class EditProductList extends AsyncTask<String, String, String> {

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
                    buffer.append("origincode").append("=").append(upOriginCode).append("&");
                    buffer.append("code").append("=").append(upCode).append("&");
                    buffer.append("class").append("=").append(upClass).append("&");
                    buffer.append("name").append("=").append(upName).append("&");
                    buffer.append("pinboxn").append("=").append(upPinboxn).append("&");
                    buffer.append("cost").append("=").append(upCost).append("&");
                    buffer.append("price").append("=").append(upPrice).append("&");
                    buffer.append("barcode").append("=").append(upBarcode);

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
            CheckProductList checkProductList = new CheckProductList();
            checkProductList.execute();
            adapter.notifyDataSetChanged();
        }

    }

    //물품 제거하는 스레드
    class DeleteProductList extends AsyncTask<String, String ,String>{
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
                    buffer.append("code").append("=").append(code);

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
            CheckProductList cTable = new CheckProductList();
            cTable.execute();
            adapter.notifyDataSetInvalidated();
        }
    }

    public void getListItemData(){
        upCode = pCode.getText().toString();
        upClass = pClass.getText().toString();
        upName = pName.getText().toString();
        upPinboxn = pPinboxn.getText().toString();
        upCost = pCost.getText().toString();
        upPrice = pPrice.getText().toString();
        upBarcode = pBarcode.getText().toString();

    }

}

package com.tony.tonythetiger.team_plane.ServerConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by tony on 2017-11-26.
 */

public class GetData extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    String errorString = null;
    String mJsonString;
    private JSONParser jsonParser;


    // 생성자
    public GetData() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //progressDialog = ProgressDialog.show(Activity.this,
        // "Please Wait", null, true, true)
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        progressDialog.dismiss();
        if(s==null){
            Log.d("main", "getData - null data");
        }else {
            mJsonString = s;
            jsonParser = new JSONParser();
        }
    }



    @Override
    protected String doInBackground(String... strings) {
        String serverURL = strings[0];

        try {
            // http connection 열기
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)
                    url.openConnection();

            // 시간 제한
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();

            // 연결 상태 확인
            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }

            // inputstream - bufferreader - sb 반환
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();


            return sb.toString().trim();

        } catch(Exception e){
            Log.d("main", "InsertData: Error ", e);
            errorString = e.toString();

            return null;
        }
    }

    public ArrayList returnData(){
        return jsonParser.returnData();
    }
}

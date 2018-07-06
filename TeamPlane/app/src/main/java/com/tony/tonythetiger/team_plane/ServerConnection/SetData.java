package com.tony.tonythetiger.team_plane.ServerConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.tony.tonythetiger.team_plane.SignIn2Activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by tony on 2017-11-26.
 */

public class SetData extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    private String postParameters;
    private JSONParser jsonParser;
    private String[] strings;
    private OnPostListener onPostListener;

    public SetData(OnPostListener onPostListener) {
        this.onPostListener = onPostListener;
    }

    // 서버로 넘길 변수 이름과 변수 설정 함수
    public void setParameters(HashMap<String, String> parameters){
        // jsonParser 미리 초기화
        jsonParser = new JSONParser();

        postParameters="";
        Set key = parameters.keySet();
        for (Iterator iterator = key.iterator(); iterator.hasNext();) {
            String keyName = (String) iterator.next();
            String valueName = parameters.get(keyName);
            if(iterator.hasNext()){
                postParameters+= keyName +"= " +valueName+"&";
            } else {
                postParameters+= keyName +"= " +valueName;
            }
        }
        Log.d("main", "post parameters - " + postParameters);

    }

    //
    public void setJsonParameter(String[] strings){
        this.strings = strings;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //progressDialog = ProgressDialog.show(SignIn2Activity,
                //"Please Wait", null, true, true);
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //progressDialog.dismiss();
        if(s==null){
            Log.d("main", "getData - null data");
        }else {
            Log.d("main", "SetData: Data return - "+s);
            jsonParser.setmJsonString(s);
            jsonParser.setParameters(strings);
            onPostListener.onPost(jsonParser.returnData());
        }
    }


    // String... params execute 함수 호출 시 매개변수
    @Override
    protected String doInBackground(String... params) {
        String serverURL = params[0];
        //String postParameters = "name=" + name + "&address=" + address;


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setRequestProperty("content-type", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d("main", "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();

            Log.d("main", "inputstream done"+sb.toString());

            return sb.toString();


        } catch (Exception e) {

            Log.d("main", "InsertData: Error ", e);

            return new String("Error: " + e.getMessage());
        }

    }

}

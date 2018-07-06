package com.tony.tonythetiger.team_plane.ServerConnection;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tony on 2017-12-04.
 */

public class JSONParser {

    private int PARAMETER_LENGTH;
    private String[] PARAMETERS;
    private String mJsonString;
    private ArrayList<HashMap<String, String>> mArrayList;
    private String TAG_JSON="teamplane";

    // 반활할 array list 반환
    public JSONParser() {
        mArrayList = new ArrayList<>();
    }

    // parse할 데이터
    public void setmJsonString(String jsonString){
        mJsonString = jsonString;
    }

    // parse 데이터에서 가져올 변수
    public void setParameters(String[] strings){
        this.PARAMETER_LENGTH = strings.length;
        this.PARAMETERS = strings;
    }

    // 태그 데이터 유동적으로 변동
    public ArrayList<HashMap<String, String>> returnData(){
        try {
            // 데이터 string empty 체크
            if(mJsonString!=null){

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                Log.d("main", "JSONParser: jsonArray - "+jsonArray.getString(0));
                for(int i=0; i<jsonArray.length(); i++){
                    Log.d("main", "JSONParser: jsonArray - "+jsonArray.length());
                    JSONObject item = jsonArray.getJSONObject(i);
                    HashMap<String, String> hashMap = new HashMap<>();

                    // j==0 is TAG_JASON
                    for(int j=0; j<PARAMETER_LENGTH; j++){
                        hashMap.put(PARAMETERS[j], item.getString(PARAMETERS[j]));
                    }
                    mArrayList.add(hashMap);

                }
                return mArrayList;
            } else{
                Log.d("main", "JSONPARSER: mJsonString is null");
            }

        } catch (JSONException e) {

            Log.d("debug", "showResult : ", e);
        }
        return null;
    }

}

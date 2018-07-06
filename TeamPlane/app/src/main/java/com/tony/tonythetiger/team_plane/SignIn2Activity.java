package com.tony.tonythetiger.team_plane;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tony.tonythetiger.team_plane.Object.Individual;
import com.tony.tonythetiger.team_plane.Object.Team;
import com.tony.tonythetiger.team_plane.Object.User;
import com.tony.tonythetiger.team_plane.ServerConnection.OnPostListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SignIn2Activity extends AppCompatActivity {

    final int popUpCode = 1001;
    final int finishCode = 2001;
    boolean team_check;

    // shared Preference mode
    int MODE_PRIVATE = 0;

    // sign in data
    private String id;
    private String pw;
    private String name;
    private String gender="0";
    private String phone;
    private String field;
    private String task;
    private String history;
    private String area;
    private String keyy;

    //spinner
    Spinner fieldSpin;
    Spinner taskSpin;
    Spinner areaSpin;
    EditText text_history;

    User user;

    // server hashmap
    HashMap<String, String> hashMap;

    // sharedPreference
    SharedPreferences sharedpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);

        // sharedpf 초기화
        sharedpf = getSharedPreferences("user", Context.MODE_PRIVATE);

        // title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign In");

        //Spinner set up
        fieldSpin = findViewById(R.id.fieldSpinner);
        taskSpin =  findViewById(R.id.taskSpinner);
        areaSpin = findViewById(R.id.locationSpinner);

        text_history = findViewById(R.id.edit_history);
        TextView text_task = findViewById(R.id.text_task);

        // get Intent info
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");

        // team_check
        team_check = getIntent().getBooleanExtra("team", false);
        if(team_check){
            user = new Team();
            text_task.setText("원하는 팀원 분야");
            text_history.setHint("팀 소개");
        } else{
            user = new Individual();
            gender = intent.getStringExtra("gender");
        }
    }

    // 다음 버튼 클릭 시
    // 개인 - 설문조사
    // 팀 - 가입 완료
    public void NextBtn(View view){
        getInfo();
        if(team_check){
            setUserPreferences();
            Toast.makeText(getApplicationContext(), "회원가입 완료하였습니다", Toast.LENGTH_SHORT).show();
            setResult(finishCode);
            finish();
        }else{
            // disc 넘어가기전에 저장해놓기
            putIndividualInfo();

            Intent intent = new Intent(getApplicationContext(), DiscPopActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("data", "whatever");
            startActivityForResult(intent, popUpCode);
        }
    }

    // disc 설문 이후 개인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // discPop 설문 완료 시
        if(resultCode==finishCode){
            Log.d("main", "SignIn2: result code - "+resultCode);
            team_check=false;
            getInfo();
            hashMap.put("d", data.getStringExtra("d"));
            hashMap.put("i", data.getStringExtra("i"));
            hashMap.put("s", data.getStringExtra("s"));
            hashMap.put("c", data.getStringExtra("c"));
            setResult(resultCode);
            SharedPreferences.Editor editor = sharedpf.edit();
            editor.putString("d",data.getStringExtra("d"));
            editor.putString("i",data.getStringExtra("i"));
            editor.putString("s",data.getStringExtra("s"));
            editor.putString("c", data.getStringExtra("c"));
            editor.apply();
            setUserPreferences();
            finish();
        }
    }

    // hashMap 데이터 가져오기
    private void getInfo(){
        field = Integer.toString( fieldSpin.getSelectedItemPosition());
        task = Integer.toString(taskSpin.getSelectedItemPosition());
        area = Integer.toString(areaSpin.getSelectedItemPosition());
        history = text_history.getText().toString();

        // team disc는 0으로 일단 set
        hashMap = new HashMap<>();
        hashMap.put("team_check", Boolean.toString(team_check));
        if(team_check){
            hashMap.put("id", id);
            hashMap.put("password", pw);
            hashMap.put("gender", gender);
            hashMap.put("name", name);
            hashMap.put("field", field);
            hashMap.put("d", "0");
            hashMap.put("i", "0");
            hashMap.put("s", "0");
            hashMap.put("c", "0");
            hashMap.put("team_number", "1");
            hashMap.put("phone", phone);
            hashMap.put("area", area);
            hashMap.put("role", field);
            hashMap.put("finding_role", task);
            hashMap.put("explanation", history);
        } else{
            // 개인의 경우 shredpref에서 가져오기
            hashMap.put("id", sharedpf.getString("id", "null"));
            hashMap.put("password", sharedpf.getString("password","null"));
            hashMap.put("gender",  sharedpf.getString("gender","null"));
            hashMap.put("name",  sharedpf.getString("name","null"));
            hashMap.put("career_interest",  sharedpf.getString("field","null"));
            hashMap.put("phone",  sharedpf.getString("phone","null"));
            hashMap.put("area",  sharedpf.getString("area","null"));
            hashMap.put("role",  sharedpf.getString("role","null"));
            hashMap.put("career",  sharedpf.getString("career","null"));
        }
    }

    // 결과가 준비되면 받음
    // sharedPreference에 저장
    private void setUserPreferences(){
        OnPostListener onPostListener = new OnPostListener() {
            @Override
            public void onPost(ArrayList<HashMap<String, String>> mArrayList) {
                if(mArrayList!=null){
                    HashMap<String,String> hashMap = mArrayList.get(0);
                    keyy = hashMap.get("keyy");

                    // save on user shared preference
                    SharedPreferences.Editor editor = sharedpf.edit();
                    editor.putString("key", keyy);
                    editor.putBoolean("team_check", team_check);
                    editor.apply();
                    Log.d("main", "user key - "+keyy);
                }else {
                    Log.d("main", "SignIn2: return data - null");
                }
            }
        };
        Log.d("main", "user.SignIn()");
        user.SignIn(hashMap, onPostListener);
    }

    // disc 넘어가기전에 sharedpreference에 개인 정보 입력
    private void putIndividualInfo(){
        SharedPreferences.Editor editor = sharedpf.edit();
        editor.putString("id",id);
        editor.putString("password",pw);
        editor.putString("gender",gender);
        editor.putString("name", name);
        editor.putString("field", field);
        editor.putString("phone", phone);
        editor.putString("area", area);
        editor.putString("role", field);
        editor.putString("career", history);
        editor.apply();
    }

}

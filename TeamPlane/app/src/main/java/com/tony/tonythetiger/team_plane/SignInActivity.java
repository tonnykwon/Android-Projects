package com.tony.tonythetiger.team_plane;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class SignInActivity extends AppCompatActivity{

    final int popUpCode = 1001;
    final int finishCode = 2001;
    boolean team_check;

    EditText edit_name;
    EditText edit_phone;
    EditText edit_ID;
    EditText edit_pw;
    RadioGroup rg;

    private String id;
    private String name;
    private String gender;
    int man = 1;
    private String phone;
    private String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sign In");

        // layout set up
        edit_name = findViewById(R.id.edit_name);
        edit_phone = findViewById(R.id.edit_phone);
        edit_ID = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);
        rg = findViewById(R.id.rg_sign_in);

        // Check team or individual
        team_check = getIntent().getBooleanExtra("team", false);
        if(team_check){
            edit_name.setHint("팀 이름");
            edit_phone.setHint("연락처");
            rg.setVisibility(View.GONE);
        }

    }

    // 다음 버튼 클릭 시
    public void NextBtn(View view){
        getInfo();
        Intent intent = new Intent(getApplicationContext(), SignIn2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("id", id);
        intent.putExtra("pw", pw);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("gender", gender);
        intent.putExtra("team", team_check);
        startActivityForResult(intent, popUpCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==finishCode){
            finish();
        }
    }

    private void getInfo(){
        name = edit_name.getText().toString();
        phone = edit_phone.getText().toString();
        id = edit_ID.getText().toString();
        pw = edit_pw.getText().toString();
        if(rg.getCheckedRadioButtonId()!=-1){
            if(rg.getCheckedRadioButtonId()==man){
                gender= "1";
            }else{
                gender="2";
            }
        } else{
            //team 경우
            gender="0";
        }
    }
}

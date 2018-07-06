package com.tony.tonythetiger.team_plane;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.tony.tonythetiger.team_plane.Kakao.SessionCallback;
import com.tony.tonythetiger.team_plane.Object.Individual;
import com.tony.tonythetiger.team_plane.Object.User;
import com.tony.tonythetiger.team_plane.ServerConnection.OnPostListener;

import java.util.ArrayList;
import java.util.HashMap;


public class LogInActivity extends AppCompatActivity {

    private SessionCallback callback;

    //sharedpreference data
    private SharedPreferences sharedpf;
    private String userKey;
    int MODE_PRIVATE = 0;

    // data
    RadioGroup rg;
    EditText edit_id;
    EditText edit_pw;
    String id;
    String pw;
    int btnCheck;
    boolean team_check; // team_check 1이면 팀

    private String keyy;

    String log = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        setContentView(R.layout.activity_log_in);

        // preference 체크
        checkSharedPreference();
        // 로그인된 사용자
        if(!userKey.equals("")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        rg = findViewById(R.id.team_rg);

        // kakao session
        callback = new SessionCallback(this);
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
        requestMe();


    }

    //로그인 버튼 클릭 시
    public void LogInBtn(View view){
        getInfo();
        if(btnCheck==-1){
            Snackbar.make(view, "팀 여부를 체크하세요", Snackbar.LENGTH_SHORT).show();
        }else {

            //listenr 설정
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

                        // login sharedpf 저장하고 진행
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        Log.d("main", "SignIn2: return data - null");
                    }
                }
            };

            // 개인 혹은 팀 로그인
            User user = new User();
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("key", userKey);
            parameters.put("team_check", Boolean.toString(team_check));
            parameters.put("id", id);
            parameters.put("password", pw);
            user.LogIn(parameters, onPostListener);
        }
    }

    //회원가입 버튼 클릭 시
    public void SignInBtn(View view){
        // 팀, 개인 여부 체크
        getInfo();

        if(btnCheck==-1){
            Snackbar.make(view, "팀 여부를 체크해주세요", Snackbar.LENGTH_SHORT).show();
        } else{
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            // team - true, individual - false;
            intent.putExtra("team", team_check);
            Log.d("main", "Log , radio button" + rg.getCheckedRadioButtonId());
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.d("kakao", message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    //redirectLoginActivity();
                }
            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("kakao", "onSessionClosed() - "+errorResult);
                //redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                String kakaoID = String.valueOf(userProfile.getId()); // userProfile에서 ID값을 가져옴
                String kakaoNickname = userProfile.getNickname();     // Nickname 값을 가져옴
                String url = String.valueOf(userProfile.getProfileImagePath());

                Log.d("kakao", "==========================");
                Log.d("kakao", ""+userProfile);
                Log.d("kakao", kakaoID);
                Log.d("kakao", kakaoNickname);
                Log.d("kakao", "==========================");
            }
        });
    }

    // get ID, PW, radio
    private void getInfo(){
        edit_id = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);

        id = edit_id.getText().toString();
        pw = edit_pw.getText().toString();
        btnCheck = rg.getCheckedRadioButtonId();
        team_check = (btnCheck== R.id.radioTeam);

    }
    private void checkSharedPreference(){
        sharedpf = getSharedPreferences("user", MODE_PRIVATE);
        userKey = sharedpf.getString("key", "");

    }

}

package com.tony.tonythetiger.team_plane;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int delayTime = 2000;

        // Handler로 꺼지는 시간 컨트롤
        Handler hd = new Handler();
        hd.postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
            }
        }, delayTime);
    }
}

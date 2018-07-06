package com.tony.tonythetiger.memind;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RelativeLayout background = (RelativeLayout) findViewById(R.id.splash);


        //이미지 입히기
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash, options);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        background.setBackground(bitmapDrawable);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();       // 2 초후 이미지를 닫아버림
            }
        }, 2000);
    }
}

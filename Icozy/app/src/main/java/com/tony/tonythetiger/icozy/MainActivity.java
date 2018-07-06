package com.tony.tonythetiger.icozy;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Splash 화면 보여주기
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.setAction(Intent.ACTION_SCREEN_OFF);
        startActivity(intent);
        setContentView(R.layout.activity_main);

        //ActionBar setting
        setActionBar();

    }

    //color button 이동
    public void colorBtn(View view){
        Intent intent = new Intent(this, LightingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //alarm button 이동
    public void alarmBtn(View view){
        Intent intent = new Intent(this, ReserveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //setting button 이동
    public void settingBtn(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //light button 클릭시
    public void lightBtn(View view){

    }

    //open button 클릭시
    public void openBtn(View view){

    }
    //close button 클릭시
    public void closeBtn(View view){

    }
    //up button 클릭시
    public void upBtn(View view){

    }
    //down button 클릭시
    public void downBtn(View view){
    }

    private void setActionBar(){
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar!=null){

            View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_main, null);
            TextView titleView = (TextView)  mCustomView.findViewById(R.id.titleBar);
            Button buttonBar = (Button) mCustomView.findViewById(R.id.buttonBar);
            titleView.setBackgroundResource(R.drawable.top_logo);
            titleView.setText("");
            buttonBar.setVisibility(View.INVISIBLE);
            bar.setCustomView(mCustomView);
            bar.setDisplayShowCustomEnabled(true);
        }

    }
}

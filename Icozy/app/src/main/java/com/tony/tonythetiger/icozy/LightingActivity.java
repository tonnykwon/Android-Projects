package com.tony.tonythetiger.icozy;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import functions.ColorPickerView;

public class LightingActivity extends AppCompatActivity{

    private static String log = "icozy";
    int pickerColor=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighting);

        setActionBar();
    }

    //설정한 color가져오기
    public void setColor(View v){
        if(pickerColor ==0){
            Toast.makeText(getApplicationContext(), "등 색을 골라주세요", Toast.LENGTH_SHORT).show();
        } else {
            LightingActivity.this.findViewById(android.R.id.content)
                    .setBackgroundColor(pickerColor);
        }
    }

    //액션바 바꾸기
    private void setActionBar() {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {

            View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_custom, null);
            TextView titleText = (TextView) mCustomView.findViewById(R.id.titleBar);
            titleText.setText("조명");
            Button barButton = (Button) mCustomView.findViewById(R.id.buttonBar);
            barButton.setBackgroundResource(R.drawable.icon_home);

            //홈버튼 클릭시 메인액티비티로 이동
            barButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            bar.setCustomView(mCustomView);
            bar.setDisplayShowCustomEnabled(true);
        }
    }

    // 전구 클릭시 색 가져오기
    public void whiteBtn(View view){
        pickerColor = Color.parseColor("#FFFFFFFF");
    }
    public void redBtn(View view){
        pickerColor = Color.parseColor("#EA2121");
    }

    public void orangeBtn(View view){
        pickerColor = Color.parseColor("#FF7900");
    }

    public void pinkBtn(View view){
        pickerColor =Color.parseColor("#F987E4");
    }

    public void yellowBtn(View view){
        pickerColor = Color.parseColor("#FFF200");
    }

    public void greenBtn(View view){
        pickerColor = Color.parseColor("#97E51C");
    }

    public void blueBtn(View view){
        pickerColor = Color.parseColor("#00C9FF");
    }
}

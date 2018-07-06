package com.tony.tonythetiger.icozy;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ReserveList.ReserveListAdapter;

public class ReserveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        //액션바 설정
        setActionBar();

        //리스트뷰 설정
        ListView listView = (ListView) findViewById(R.id.listView);

        ReserveListAdapter adapter = new ReserveListAdapter();
        adapter.addItem("오전","7:30");
        adapter.addItem("오후","4:00");
        adapter.addItem("오후","8:40");

        listView.setAdapter(adapter);
    }


    private void setActionBar() {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {

            View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_custom, null);
            TextView titleText = (TextView) mCustomView.findViewById(R.id.titleBar);
            titleText.setText("예약");
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
}

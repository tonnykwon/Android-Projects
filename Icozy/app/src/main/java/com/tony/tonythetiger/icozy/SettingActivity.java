package com.tony.tonythetiger.icozy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class SettingActivity extends AppCompatActivity {

    private boolean energyOn = true;
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setActionBar();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    private void setActionBar() {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {

            View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_custom, null);
            TextView titleText = (TextView) mCustomView.findViewById(R.id.titleBar);
            titleText.setText("설정");
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

    public void energyBtn(View view){
        if(energyOn){
            view.setBackgroundResource(R.drawable.icon_off);
            energyOn=false;
        } else{
            view.setBackgroundResource(R.drawable.icon_on);
            energyOn=true;
        }
    }

    public void blueToothBtn(View view){

        if(btAdapter==null){
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않습니다", Toast.LENGTH_SHORT).show();
        } else if(btAdapter.getState()== BluetoothAdapter.STATE_OFF){

            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, BluetoothAdapter.STATE_ON);

        } else if(btAdapter.getState()== BluetoothAdapter.STATE_ON){

            Intent intent = new Intent(this, BluetoothActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "연결 기기 찾는중", Toast.LENGTH_SHORT).show();
        }
    }
}

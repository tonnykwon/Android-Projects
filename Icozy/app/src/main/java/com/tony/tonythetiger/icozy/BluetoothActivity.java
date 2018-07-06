package com.tony.tonythetiger.icozy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private ArrayAdapter<String> BTArrayAdapter;
    private ListView listView;
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        setActionBar();

        //블루투스 기기들 가져오기
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        pairedDevices = btAdapter.getBondedDevices();
        if(pairedDevices!=null){
            for(BluetoothDevice device : pairedDevices){
                BTArrayAdapter.add(device.getName()+ "\n" + device.getAddress());
            }
        }
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(BTArrayAdapter);



    }

    private void setActionBar() {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if (bar != null) {

            View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_custom, null);
            TextView titleText = (TextView) mCustomView.findViewById(R.id.titleBar);
            titleText.setText("블루투스");
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

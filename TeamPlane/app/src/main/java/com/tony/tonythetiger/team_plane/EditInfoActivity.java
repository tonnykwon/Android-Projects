package com.tony.tonythetiger.team_plane;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        // title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Information");

    }
}

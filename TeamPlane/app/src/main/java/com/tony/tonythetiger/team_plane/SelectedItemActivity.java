package com.tony.tonythetiger.team_plane;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.tony.tonythetiger.team_plane.SelectedList.SelectedItem;
import com.tony.tonythetiger.team_plane.SelectedList.SelectedListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectedItemActivity extends AppCompatActivity {


    List<SelectedItem> itemsData;

    RecyclerView recyclerView;
    SelectedListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // recylcerView 데이터 가져오기
        SetRecyclerView();

        // set up
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("팀 이름");
        ImageView selected_image = findViewById(R.id.selected_image);

        // 팀 추가 혹은 개인 추가
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "추가 신청을 보냈습니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // 참가 신청 php 연결
            }
        });

    }

    // list 준비
        public void SetRecyclerView(){
            Intent intent = getIntent();
            String key = intent.getStringExtra("key");
            String name = intent.getStringExtra("name");
            String field= intent.getStringExtra("field");
            String subField= intent.getStringExtra("subField");
            String intro = intent.getStringExtra("intro");
            String area = intent.getStringExtra("area");
            String d = intent.getStringExtra("d");
            String i = intent.getStringExtra("i");
            String s = intent.getStringExtra("s");
            String c = intent.getStringExtra("c");
            recyclerView =  findViewById(R.id.selectedList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            // list 내용물 넣기
            if(itemsData!=null){
                itemsData.clear();
            } else{
                itemsData = new ArrayList<SelectedItem>();
            }

        // adapter 적용
        itemsData.add(new SelectedItem("Name", name));
        itemsData.add(new SelectedItem("Field", field));
        itemsData.add(new SelectedItem("Subfield", subField));
        itemsData.add(new SelectedItem("History", intro));
        itemsData.add(new SelectedItem("Location", area));
        itemsData.add(new SelectedItem("D", d));
        itemsData.add(new SelectedItem("I", i));
        itemsData.add(new SelectedItem("S", s));
        itemsData.add(new SelectedItem("C", c));
        adapter = new SelectedListAdapter(itemsData);
        recyclerView.setAdapter(adapter);

        // scroll control
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
    }
}

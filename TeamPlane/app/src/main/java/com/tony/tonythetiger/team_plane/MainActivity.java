package com.tony.tonythetiger.team_plane;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tony.tonythetiger.team_plane.Object.Individual;
import com.tony.tonythetiger.team_plane.Object.Team;
import com.tony.tonythetiger.team_plane.Object.User;
import com.tony.tonythetiger.team_plane.ServerConnection.OnPostListener;
import com.tony.tonythetiger.team_plane.ViewList.ViewItem;
import com.tony.tonythetiger.team_plane.ViewList.ViewListAdapter;
import com.tony.tonythetiger.team_plane.functions.ItemClickSupport;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // sharedPreferences
    private SharedPreferences sharedpf;
    private String userKey = null;
    int MODE_PRIVATE = 0;
    private boolean team_check;
    private String d;
    private String I;
    private String s;
    private String c;


    User user;

    List<ViewItem> itemsData;

    RecyclerView recyclerView;
    ViewListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // title set
        getSupportActionBar().setTitle("Main");

        // sharedPreferences 체크
        checkSharedPreferences();

        // recycler list 불러오기
        SetRecyclerView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //list item click 시
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), SelectedItemActivity.class);
                String key = itemsData.get(position).getKey();
                String name = itemsData.get(position).getName();
                String field = itemsData.get(position).getField();
                String subField = itemsData.get(position).getSubField();
                String intro = itemsData.get(position).getIntro();
                String getD = itemsData.get(position).getD();
                String getI = itemsData.get(position).getI();
                String getS = itemsData.get(position).getS();
                String getC = itemsData.get(position).getC();
                String area = itemsData.get(position).getArea();
                intent.putExtra("key", key);
                intent.putExtra("name", name);
                intent.putExtra("field", field);
                intent.putExtra("subField", subField);
                intent.putExtra("intro", intro);
                intent.putExtra("d", getD);
                intent.putExtra("i", getI);
                intent.putExtra("s", getS);
                intent.putExtra("c", getC);
                intent.putExtra("area", area);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Drawerlayout 켜져있을 시 drawer 끔
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // navigator menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_home){
            // 홈 리스트 보이기
            // 데이터 새로 요청
            getSupportActionBar().setTitle("Main");
            SetRecyclerView();

        }else if (id == R.id.nav_edit) {
            // 정보 변경 메뉴
            Intent intent = new Intent(getApplicationContext(), EditInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        } else if(id==R.id.nav_request){
            // 요청 받은 리스트로 새로 고침
            // 데이터 새로 요청
            itemsData.clear();
            itemsData.add(new ViewItem("id ", "field", "subField","subfield","intro","4","4","2", "2","0.2","area", 1) );
            adapter = new ViewListAdapter(itemsData);
            recyclerView.setAdapter(adapter);

            getSupportActionBar().setTitle("Request");

        } else if (id == R.id.nav_logout) {
            // 로그아웃 다이로그
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            // 메세지
            // 확인 버튼 리스너
            alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // sharedpreference 삭제
                    sharedpf.edit().remove("key").apply();
                    sharedpf.edit().clear().apply();
                    Log.d("main", "Main: log out sharedpf - "+sharedpf.getString("key", "deleted"));
                    finish();
                }
            });
            alert_confirm.setNegativeButton("취소", null);
            // 다이얼로그 생성
            AlertDialog alert = alert_confirm.create();
            // 아이콘
            //alert.setIcon(R.drawable.ic_launcher);
            // 다이얼로그 타이틀
            alert.setTitle("로그아웃 하시겠습니까?");
            // 다이얼로그 보기
            alert.show();


        } else if (id == R.id.nav_out) {
            // 탈퇴 다이로그
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            // 확인 버튼 리스너
            alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // sharedpreference 삭제
                    // 회원 데이터 인스턴스 삭제
                    finish();
                }
            });
            alert_confirm.setNegativeButton("취소", null);
            AlertDialog alert = alert_confirm.create();
            alert.setTitle("탈퇴하시겠습니까?");
            alert.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void SetRecyclerView(){
        recyclerView = findViewById(R.id.viewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // list 내용물 넣기
        if(itemsData!=null){
            itemsData.clear();
        } else{
            itemsData = new ArrayList<ViewItem>();
        }

        // 리스트 데이터 가져오기

        if(team_check){
            user = new Team();
        } else {
            user = new Individual();
        }
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("keyy", userKey);
        parameters.put("team_check", Boolean.toString(team_check));
        OnPostListener onPostListener = new OnPostListener() {
            @Override
            public void onPost(ArrayList<HashMap<String, String>> mArrayList) {
                // 가져온 데이터 itemsData에 넣기
                //recyclerView.removeAllViews();
                if(mArrayList!=null){
                    for(int i = 0; i <mArrayList.size(); i++){
                        Log.d("main", "Main: mArrayList size - "+mArrayList.size());
                        HashMap<String, String> hashMap = mArrayList.get(i);
                        //float sim = getSim(getD, getI, getS, getC);
                        float sim = 0.8f-0.04f*i;
                        if(team_check){
                            itemsData.add(new ViewItem(hashMap.get("keyy"),
                                    hashMap.get("name"),
                                    getField(hashMap.get("career_interest")),
                                    hashMap.get("role"),
                                    hashMap.get("career"),
                                    hashMap.get("d"),
                                    hashMap.get("i"),
                                    hashMap.get("s"),
                                    hashMap.get("c"),
                                    Float.toString(sim)+"%",
                                    hashMap.get("area"),
                                    (int) (Math.random() * 8)+1));
                        } else{
                            itemsData.add(new ViewItem(hashMap.get("keyy"),
                                    hashMap.get("name"),
                                    getField(hashMap.get("field")),
                                    hashMap.get("finding_role"),
                                    hashMap.get("explanation"),
                                    hashMap.get("d"),
                                    hashMap.get("i"),
                                    hashMap.get("s"),
                                    hashMap.get("c"),
                                    Float.toString(sim)+"%",
                                    hashMap.get("area"),
                                    (int) (Math.random() * 8)+1
                                    ));
                        }
                    }
                    adapter = new ViewListAdapter(itemsData);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "매칭되는 팀 혹은 개인이 없습니다." ,Toast.LENGTH_SHORT).show();
                }
            }
        };
        user.matching(parameters, onPostListener);


    }

    // sharedpreferences 보기
    private void checkSharedPreferences(){
        sharedpf = getSharedPreferences("user", MODE_PRIVATE);
        team_check = sharedpf.getBoolean("team_check", false);
        userKey = sharedpf.getString("key", "");
        d = sharedpf.getString("d","4");
        I = sharedpf.getString("i","3");
        s = sharedpf.getString("s","5");
        c = sharedpf.getString("c","4");
    }
/*


    public float getSim(int getD, int getI, int getS, int getC){
        Log.d("main", "d - "+d+", i - "+I);
        Log.d("main", "getD - "+getD+", getI - "+getI);
        float nominator = d*getD+I*getI+s*getS+c*getC;
        double denominator = Math.sqrt( Math.pow(d,2)+Math.pow(I,2)+Math.pow(s,2)+Math.pow(c,2))
                *Math.sqrt(Math.pow(getD,2)+Math.pow(getI,2)+Math.pow(getS,2)+Math.pow(getC,2));
        return nominator/(float)denominator;
    }
 */
    public String getField(String field){
        Resources resources = getResources();
        String[] array = resources.getStringArray(R.array.field_array);
        return array[Integer.parseInt(field)];
    }
}

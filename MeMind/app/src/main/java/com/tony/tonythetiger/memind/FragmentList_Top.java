package com.tony.tonythetiger.memind;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tony.tonythetiger.memind.MemoryList.MemoryItem;
import com.tony.tonythetiger.memind.MemoryList.MemoryListAdapter;

import java.util.ArrayList;
import java.util.List;

import functions.DatabaseHelper;
import functions.ItemClickSupport;


public class FragmentList_Top extends android.support.v4.app.Fragment {

    DatabaseHelper helper;
    SQLiteDatabase database;
    String dbName;
    List<MemoryItem> itemsData;

    RecyclerView recyclerView;
    MemoryListAdapter adapter;
    View view;

    boolean isDateSelected;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbName = "MeMind";
        helper = new DatabaseHelper(getActivity().getApplicationContext(), dbName, null, 1);
        database = helper.getWritableDatabase();

        //기본 정렬 날짜
        isDateSelected = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fragment_list__top, container, false);

        //textview 참조
        TextView dateBtn = (TextView) view.findViewById(R.id.dateBtn);
        TextView starBtn = (TextView) view.findViewById(R.id.starBtn);

        //date버튼 클릭시
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateSelected = true;
                SetRecyclerView();
            }
        });

        //star버튼 클릭시
        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateSelected = false;
                SetRecyclerView();
            }
        });

        //item click시
        SetRecyclerView();
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                int type = adapter.getItemViewType(position);
                if (type == 0) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SelectedItemActivity.class);
                    int id = itemsData.get(position).getId();
                    intent.putExtra("id", id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(intent);
                }
            }
        });

        //long click 시
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, final int position, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("삭제하시겠습니까?");
                builder.setCancelable(true);
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = itemsData.get(position).getId();
                        database.execSQL("DELETE FROM " + dbName + " WHERE id = " + id);
                        SetRecyclerView();
                    }
                });
                builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        //이미지 입히기
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.top_b, options);
        RelativeLayout top = (RelativeLayout) view.findViewById(R.id.background);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        top.setBackground(bitmapDrawable);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SetRecyclerView();
    }

    public void SetRecyclerView(){

        recyclerView = (RecyclerView) view.findViewById(R.id.memoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        if(itemsData!=null){
            itemsData.clear();
        } else {
            itemsData = new ArrayList<MemoryItem>();
        }

        if(database != null){
            if(isDateSelected){

                //distinct date 가져오기
                Cursor dateCursor = database.rawQuery("SELECT strftime('%Y-%m', thedate) FROM " + dbName
                        +" GROUP BY strftime('%Y-%m', thedate) ORDER BY thedate DESC", null);

                Cursor itemCountCursor = database.rawQuery("SELECT * FROM " + dbName, null);
                Log.d("maindate", "item count - " + itemCountCursor.getCount() + ", date count - " + dateCursor.getCount());

                dateCursor.moveToFirst();
                for(int i=0; i<dateCursor.getCount(); i++){

                    String distinctDate = dateCursor.getString(0);
                    itemsData.add(new MemoryItem(1, distinctDate));

                    Log.d("maindate", "distinct month - "+distinctDate);

                    //date별 자료 가져오기
                    Cursor cursor = database.rawQuery("SELECT * FROM " + dbName+
                            " WHERE thedate LIKE '"+distinctDate+"-%'", null);
                    cursor.moveToFirst();
                    for(int j=1; j<cursor.getCount()+1; j++){

                        int id = cursor.getInt(0);
                        String question = cursor.getString(1);
                        String exprience = cursor.getString(2);
                        String date = cursor.getString(3);
                        float star = cursor.getFloat(4);

                        Log.d("maindate", "question - "+question+", exprience- "+exprience+", date - "+date);
                        itemsData.add(new MemoryItem(id, question, exprience, date, star));


                        cursor.moveToNext();
                    }
                    cursor.close();
                    dateCursor.moveToNext();
                }
                dateCursor.close();

                adapter = new MemoryListAdapter(itemsData);
                recyclerView.setAdapter(adapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

            } else {

                //distinct star 가져오기
                Cursor starCursor = database.rawQuery("SELECT DISTINCT star FROM " + dbName +" ORDER BY star DESC", null);

                Cursor itemCountCursor = database.rawQuery("SELECT * FROM " + dbName, null);
                Log.d("Main", "item count - "+itemCountCursor.getCount()+", star count - "+starCursor.getCount());

                starCursor.moveToFirst();
                for(int i=0; i<starCursor.getCount(); i++){

                    Float distinctStar = starCursor.getFloat(0);
                    itemsData.add(new MemoryItem(2, distinctStar, getActivity().getApplicationContext()));

                    //date별 자료 가져오기
                    Cursor cursor = database.rawQuery("SELECT * FROM " + dbName +
                            " WHERE star = "+ distinctStar, null);
                    cursor.moveToFirst();
                    for(int j=1; j<cursor.getCount()+1; j++){

                        int id = cursor.getInt(0);
                        String question = cursor.getString(1);
                        String exprience = cursor.getString(2);
                        String date = cursor.getString(3);
                        float star = cursor.getFloat(4);

                        Log.d("Main", "question - "+question+", exprience- "+exprience+", date - "+date);
                        itemsData.add(new MemoryItem(id, question, exprience, date, star));


                        cursor.moveToNext();
                    }
                    cursor.close();
                    starCursor.moveToNext();
                }
                starCursor.close();

                adapter = new MemoryListAdapter(itemsData);
                recyclerView.setAdapter(adapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

            }
        }
    }


}

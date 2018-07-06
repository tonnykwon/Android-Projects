package com.tony.tonythetiger.memind;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import functions.DatabaseHelper;


public class FragmentShow_Mid extends android.support.v4.app.Fragment {


    DatabaseHelper helper;
    SQLiteDatabase database;
    String dbName;

    boolean questionClicked;
    boolean animEnd;
    boolean dbNull;

    String question;
    String experience;
    int id;
    float ratingStar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionClicked = false;
        animEnd = true;

        //db 열기
        dbName = "MeMind";
        helper = new DatabaseHelper(getActivity().getApplicationContext(), dbName, null, 1);
        database = helper.getWritableDatabase();
        dbNull = dbNullCheck();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //참조
        final View view = inflater.inflate(R.layout.fragment_fragment_show__mid, container, false);
        final TextView today = (TextView) view.findViewById(R.id.todayText);
        final TextView expText = (TextView) view.findViewById(R.id.expText);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        if(dbNull){
            question = "스스로에게 어떤 질문을 하셨나요?";
            experience = "오늘 어떤 일을 경험하셨나요? ";
        } else {
            checkPreference(dateCheck());
        }

        //todayText 세팅
        today.setText(question);

        //transDown 위치 바꾸기
        final Animation transDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.trans_down);
        transDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!questionClicked) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = today.getTop() + today.getHeight();
                    params.leftMargin = today.getLeft();
                    today.setLayoutParams(params);
                }
                //remove the animation, its no longer needed, since button is really there
                today.clearAnimation();
                animEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //transUp 위치 바꾸기
        final Animation transUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.trans_up);
        transUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(questionClicked){
                    //move real
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = today.getTop() - today.getHeight();
                    params.leftMargin = today.getLeft();
                    today.setLayoutParams(params);
                }
                //remove the animation, its no longer needed, since button is really there
                today.clearAnimation();
                animEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //애니메이션 적용
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animEnd) {
                    if (!questionClicked) {
                        animEnd = false;
                        today.startAnimation(transUp);
                        expText.setText(experience);
                        ratingBar.setRating(ratingStar);

                        Handler hd = new Handler();
                        hd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (questionClicked) {
                                    expText.setVisibility(View.VISIBLE);
                                    ratingBar.setVisibility(View.VISIBLE);
                                }
                            }
                        }, 1000);
                        questionClicked = true;
                    } else {
                        animEnd = false;
                        today.startAnimation(transDown);
                        expText.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.GONE);
                        questionClicked = false;
                    }
                }
            }
        });

        //image 입히기

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mid_b, options);
        FrameLayout middle = (FrameLayout) view.findViewById(R.id.background);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        middle.setBackground(bitmapDrawable);


        //ratingbar changeListener
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (database != null) {

                    database.execSQL("UPDATE " + dbName + " SET star = " + rating + " WHERE id = '" + id + "'");
                    getTodayText(false);
                }
            }
        });

        return view;
    }

    public void getTodayText(boolean newold) {

        Log.d("dbCheck", "getTodaycalled, -"+newold);

        if(newold){

            //각 star 빈도 구하기
            Cursor cursor = database.rawQuery("SELECT star, count(star) FROM " + dbName +
                    " GROUP BY star ORDER BY star DESC", null);
            cursor.moveToFirst();

            List wholen = new ArrayList<>();
            List wholenRange = new ArrayList();
            for (int j = 0; j < cursor.getCount(); j++) {

                float starD = cursor.getFloat(0);
                int starCount = cursor.getInt(1);
                Log.d("Main", "star id - " + starD + ", count - " + starCount);
                wholen.add(j, starD);
                wholenRange.add(j, starD*starCount);
                cursor.moveToNext();

            }
            cursor.close();

            //랜덤 결정된 별
            float random = (float) (Math.random() * sum(wholenRange));
            Log.d("Main", "random - " + random);
            float star = getRandom(random, wholen, wholenRange);
            Log.d("Main", "getRandom - " + star);
            Cursor randomCursor = database.rawQuery("SELECT id, question, experience, star FROM " + dbName
                    + " WHERE star = " + star + " ORDER BY RANDOM() LIMIT 1", null);
            randomCursor.moveToFirst();
            if(randomCursor.getCount()>0){
                id = randomCursor.getInt(0);
                question = randomCursor.getString(1);
                experience = randomCursor.getString(2);
                ratingStar = randomCursor.getFloat(3);
            }
            randomCursor.close();

        } else {
            Cursor todayCursor = database.rawQuery("SELECT id, question, experience, star FROM "
                    + dbName+" WHERE id = "+id, null);
            todayCursor.moveToFirst();
            if(todayCursor.getCount()>0){
                id = todayCursor.getInt(0);
                question = todayCursor.getString(1);
                experience = todayCursor.getString(2);
                ratingStar = todayCursor.getFloat(3);
            }
            todayCursor.close();
        }
    }

    public static float sum(List list) {
        float sum = 0;

        for (int i = 0; i < list.size(); i++)
            sum += (float) list.get(i);

        return sum;
    }

    public static float getRandom(float random, List starList, List randomList) {

        float compare = 0;
        float added = 0;
        for (int i = 0; i < randomList.size(); i++) {
            if (random < (float) randomList.get(i) + compare) {
                added = (float)starList.get(i);
                break;
            } else {
                compare += (float) randomList.get(i);
            }
        }
        return added;
    }

    //오늘 날짜 가져오기
    public String dateCheck(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyyMMdd");
        String strCurDate = CurDateFormat.format(date);
        Log.d("dbCheck", "today - "+strCurDate);

        return strCurDate;
    }

    //오늘의 질문 여부 체크
    public void checkPreference(String date){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String callValue = pref.getString("date", "0");
        if(!date.equals(callValue)){
            SharedPreferences.Editor editor = pref.edit();
            getTodayText(true);
            editor.putInt("id", id);
            editor.putString("date", date);
            editor.apply();
            Log.d("dbCheck", "date different -"+callValue);

        } else if(date.equals(callValue)){
            id = pref.getInt("id", -1);
            getTodayText(idNullCheck());
            Log.d("dbCheck", "date same - "+callValue+", id - "+id);
        }
    }

    //id 삭제시 새로 추가
    public boolean idNullCheck(){
        Cursor checkCursor = database.rawQuery("SELECT * FROM "+ dbName+" WHERE id = "+id, null);
        if(checkCursor.getColumnCount()==0){
            checkCursor.close();
            return true;
        } else {
            checkCursor.close();
            return false;
        }
    }

    public boolean dbNullCheck(){

        //db행 0개 체크
        Cursor checkCursor = database.rawQuery("SELECT id FROM "+dbName,null);
        checkCursor.moveToFirst();

        if(checkCursor.getCount()==0){
            checkCursor.close();
            Log.d("dbCheck", "db null");
            return true;
        } else {
            checkCursor.close();
            Log.d("dbCheck", "db not null");
            return false;
        }

    }


}

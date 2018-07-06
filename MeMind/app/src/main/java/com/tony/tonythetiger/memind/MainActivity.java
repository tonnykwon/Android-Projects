package com.tony.tonythetiger.memind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.tony.tonythetiger.memind.view.pager.VerticalPagerAdapter;
import com.tony.tonythetiger.memind.view.pager.VerticalViewPager;

import functions.BackPressCloseHandler;
import tutorials.TutorialMidFragment;

public class MainActivity extends AppCompatActivity implements TutorialMidFragment.OnFragmentInteractionListener {

    VerticalViewPager mViewPager;
    VerticalPagerAdapter mAdapter;
    BackPressCloseHandler backbutton;

    FragmentManager manager;
    TutorialMidFragment midFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        setContentView(R.layout.activity_main);

        isFirst();

        //backbutton 누를시
        backbutton = new BackPressCloseHandler(this);


        //키보드 위로 edittext 위로 올리기
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //verticalpager
        mViewPager = (VerticalViewPager) findViewById(R.id.viewPager);
        mAdapter = new VerticalPagerAdapter(getApplicationContext(), getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    InputMethodManager manager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } else if(position==2){
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            int item = mViewPager.getCurrentItem();
            if(item==0){
                mViewPager.setCurrentItem(1);
                return false;
            } else if(item==2){
                mViewPager.setCurrentItem(1);
                return false;
            } else {
                backbutton.onBackPressed();
                return false;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //tutorial check
    public void isFirst(){


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean callValue = pref.getBoolean("first", true);
        if(callValue){
            //tutorial 생성

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        midFragment = new TutorialMidFragment();
        transaction.add(android.R.id.content, midFragment, "mid_tutorial_fragment");
        transaction.commit();

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("first", false);
        editor.apply();
        }
        Log.d("fragment", pref.getBoolean("first",true)+" - ");
    }

    //tutorial fragment interaction
    @Override
    public void onFragmentInteraction(int number) {
        if(number==0){
            mViewPager.setCurrentItem(1);
        } else if(number==1){
            mViewPager.setCurrentItem(2);
        } else if(number==2){
            mViewPager.setCurrentItem(0);
        } else if(number>2){
            mViewPager.setCurrentItem(1);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(midFragment);
            transaction.commit();
            Log.d("fragment", number+" > 2");
        }
    }
}

package com.tony.tonythetiger.memind.view.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tony.tonythetiger.memind.FragmentList_Top;
import com.tony.tonythetiger.memind.FragmentShow_Mid;
import com.tony.tonythetiger.memind.FragmentWrite_Bottom;

/**
 * Created by tony on 2016-02-04.
 */

//VerticalViewPager 어댑터
public class VerticalPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public VerticalPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3; //이미지 개수 리턴(그림이 10개라서 10을 리턴)
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:

                // List 프래그먼트

                return new FragmentList_Top();

            case 1:

                // Write 프래그먼트

                return new FragmentShow_Mid();

            case 2:

                // Write 프래그먼트

                return new FragmentWrite_Bottom();



        }
        return null;
    }
}

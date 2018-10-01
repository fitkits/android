package com.fitkits.OnBoarding.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fitkits.OnBoarding.Fragments.OnBoardFragment_1;
import com.fitkits.OnBoarding.Fragments.OnBoardFragment_2;
import com.fitkits.OnBoarding.Fragments.OnBoardFragment_3;
import com.fitkits.OnBoarding.Fragments.OnBoardFragment_4;
import com.fitkits.OnBoarding.Fragments.OnBoardFragment_5;
import com.fitkits.OnBoarding.Fragments.OnBoardFragment_6;
import com.fitkits.OnBoarding.Fragments.OnBoardFragment_7;

/**
 * Adapter for linking the the Build your own layout with the tabbed viewpager layout.
 * @author Peach Studio
 * @version 1.0
 * @since 1.0
 */
public class OnboardViewPagerAdapter extends FragmentPagerAdapter {
    Fragment fragment;
    Bundle bundle;

    private Context context;
    String type;

    /**
     * Class constructor for assigning values to the viewpager attributes.
     * @param fm Fragment Manager for the view pager.
     * @param context Context of the view pager.
     */
    public OnboardViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;


    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            fragment = new OnBoardFragment_1();
        else if(position==1)
            fragment = new OnBoardFragment_2();

        else if(position==2)
            fragment = new OnBoardFragment_3();

        else if(position==3)
            fragment = new OnBoardFragment_4();

        else if(position==4)
            fragment = new OnBoardFragment_5();

        else if(position==5)
            fragment = new OnBoardFragment_6();
        else if(position==6)
            fragment = new OnBoardFragment_7();

        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }


}

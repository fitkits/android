package com.fitkits;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

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
            fragment = new fragment_onboard1();
        else if(position==1)
            fragment = new fragment_onboard2();

        else if(position==2)
            fragment = new fragment_onboard3();

        else if(position==3)
            fragment = new fragment_onboard4();

        else if(position==4)
            fragment = new fragment_onboard5();

        else if(position==5)
            fragment = new fragment_onboard6();
        else if(position==6)
            fragment = new fragment_onboard7();

        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }


}

package com.fitkits.Home.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fitkits.Home.Fragments.fragment_banner1;
import com.fitkits.Home.Fragments.fragment_banner2;
import com.fitkits.Home.Fragments.fragment_banner3;

/**
 * Adapter for linking the the Build your own layout with the tabbed viewpager layout.
 * @author Peach Studio
 * @version 1.0
 * @since 1.0
 */
public class BannerViewPagerAdapter extends FragmentPagerAdapter {
    Fragment fragment;
    Bundle bundle;

    private Context context;
    String type;

    /**
     * Class constructor for assigning values to the viewpager attributes.
     * @param fm Fragment Manager for the view pager.
     * @param context Context of the view pager.
     */
    public BannerViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;


    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            fragment = new fragment_banner1();
        else if(position==1)
            fragment = new fragment_banner2();

        else if(position==2)
            fragment = new fragment_banner3();



        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }


}

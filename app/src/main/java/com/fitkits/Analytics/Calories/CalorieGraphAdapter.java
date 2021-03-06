package com.fitkits.Analytics.Calories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for linking the BannerFragment layout with the banner viewpager in HomeFragment layout.
 * @author Peach Studio
 * @version 1.0
 * @since 1.0
 */
public class CalorieGraphAdapter extends FragmentPagerAdapter {
    Bundle bundle;
    int length;
    public static List<String> tabList=new ArrayList<String>();

    /**
     * Class constructor for assigning values to the viewpager attributes.
     * @param childFragmentManager View pager container fragment manager.

     */
    public CalorieGraphAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);

        tabList.clear();
        tabList.add("weekly");
        tabList.add("monthly");
        tabList.add("quarterly");
        tabList.add("yearly");


    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=new CalorieGraphFragment();
        Bundle bn = new Bundle();
        bn.putBundle("bundle", bundle);
        bn.putInt("position",position);
        fragment.setArguments(bn);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabList.get(position);
    }
}

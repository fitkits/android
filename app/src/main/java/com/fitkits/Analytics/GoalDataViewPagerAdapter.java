package com.fitkits.Analytics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Adapter for linking the BannerFragment layout with the banner viewpager in HomeFragment layout.
 * @author Peach Studio
 * @version 1.0
 * @since 1.0
 */
public class GoalDataViewPagerAdapter extends FragmentPagerAdapter {
    Bundle bundle;
    int length;
    ArrayList<GoalData> goalDataArrayList =new ArrayList<GoalData>();
    public static List<String> tabList=new ArrayList<String>();
    List<List<String>> weekinMonth;
    String type;

    /**
     * Class constructor for assigning values to the viewpager attributes.
     * @param childFragmentManager View pager container fragment manager.

     */
    public GoalDataViewPagerAdapter(FragmentManager childFragmentManager,ArrayList<GoalData> goalDataArrayList,List<List<String>> weekinMonth,String type) {
        super(childFragmentManager);
        this.goalDataArrayList =goalDataArrayList;
        this.weekinMonth=weekinMonth;
        this.type=type;
        tabList.clear();
        Collections.reverse(weekinMonth);
        tabList.add("THIS\nWEEK");
        tabList.add("LAST\nWEEk");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format_new_half = new SimpleDateFormat("dd");
        SimpleDateFormat format_new_full = new SimpleDateFormat("dd\nMMM");


        for(int i=2;i<weekinMonth.size();i++){
            try {
                Date startDate=format.parse(weekinMonth.get(i).get(0));
                Date endDate=format.parse(weekinMonth.get(i).get(1));
                tabList.add(format_new_half.format(startDate)+"-"+format_new_full.format(endDate).toUpperCase());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=new GoalDataFragment();
        Gson gson = new Gson();
        String json = gson.toJson(weekinMonth);
        Bundle bn = new Bundle();
        bn.putInt("position",position);
        bn.putString("dates",json);
        bn.putString("type",type);

        fragment.setArguments(bn);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabList.get(position);
    }
}

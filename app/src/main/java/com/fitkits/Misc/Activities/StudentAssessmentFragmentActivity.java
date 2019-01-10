package com.fitkits.Misc.Activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fitkits.Misc.Fragments.StatsAssessmentFragment;
import com.fitkits.Misc.RetroClient;
import com.fitkits.R;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.chart.Activities.MyStatsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class StudentAssessmentFragmentActivity extends Fragment {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_assessment_fragment);
//
//        // Instantiate a ViewPager and a PagerAdapter.

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.activity_student_assessment_fragment , container, false);
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        SharedPreferences myPrefs;
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager(),myPrefs);
        mPager.setAdapter(mPagerAdapter);
        return rootView;
    }

//    @Override
//    public void onBackPressed() {
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
//    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ApiService apiService;
        SharedPreferences myPrefs;
        ArrayList<Scores> allAssessment ;
        Integer totalAssessments;
        public ScreenSlidePagerAdapter(FragmentManager fm, SharedPreferences myPerfs) {
            super(fm);
            this.myPrefs = myPerfs;
            allAssessment = new ArrayList<>(1);
//            getAssessments();
//            totalAssessments = allAssessment.size();
        }

        @Override
        public Fragment getItem(int position) {
            StatsAssessmentFragment frg = new StatsAssessmentFragment();
            Scores s = getCurrentPageDetails(position);
            Bundle b = new Bundle();
            b.putString("balanceScore", s.balanceScore );
            b.putString("cardioScore", s.cardioScore );
            b.putString("flexibilityScore", s.flexibilityScore );
            b.putString("muscleScore", s.muscleScore );
            b.putString("postureScore", s.postureScore );
            b.putString("strengthScore", s.strengthScore );
            b.putString("lastUpdated", s.lastUpdated);
            frg.setArguments(b);
//            frg.updateScores();
            return frg;
        }

        @Override
        public int getCount() {
            Log.e("RAM", "ARRAY" +MyStatsActivity.allAssesments.size() + MyStatsActivity.allAssesments.toString());
            return MyStatsActivity.allAssesments.size();
        }

        public Scores getCurrentPageDetails(int position) {

            return MyStatsActivity.allAssesments.get(position);
        }

//        public ArrayList<Scores> getAssessments() {
//            apiService = RetroClient.getApiService(myPrefs.getString("token", ""), getContext());
//            ArrayList<Scores> allAssesments = new ArrayList<>(1);
//            Log.e("RAM", "GETTING ASSESSMENT");
//
//            apiService.getAssessments(("/api/v1/cms/studentAssessments?user=" + myPrefs.getString("user_id", "") + "&page=1&perPageCount=2000")).subscribeOn(
//                    Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
//
//                @Override
//                public void onSubscribe(Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(String value) {
//                    Scores scores = new Scores("", "", "", "", "", "");
//                    try {
//                        JSONObject rawAssessment = new JSONObject(value);
//                        JSONArray assesment = rawAssessment.getJSONArray("StudentAssessments");
//                        try {
//                            scores.balanceScore = assesment.getJSONObject(0).getString("balanceScore");
//                        } catch (Exception e) {
//
//                        }
//                        try {
//                            scores.flexibilityScore = assesment.getJSONObject(0).getString("flexibilityScore");
//                        } catch (Exception e) {
//
//                        }
//                        try {
//                            scores.strengthScore = assesment.getJSONObject(0).getString("strengthScore");
//                        } catch (Exception e) {
//
//                        }
//                        try {
//                            scores.muscleScore = assesment.getJSONObject(0).getString("muscleScore");
//                        } catch (Exception e) {
//
//                        }
//                        try {
//                            scores.cardioScore = assesment.getJSONObject(0).getString("cardioScore");
//                        } catch (Exception e) {
//
//                        }
//                        try {
//                            scores.postureScore = assesment.getJSONObject(0).getString("postureScore");
//                        } catch (Exception e) {
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    allAssesments.add(scores);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
//            return  allAssesments;
//        }
    }
}

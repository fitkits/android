package com.fitkits;

import static com.fitkits.HomeActivity.homeActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Analytics.ActiveHours.ActiveHoursGraphActivity;
import com.fitkits.Analytics.Calories.CalorieGraphActivity;
import com.fitkits.Analytics.Sleep.SleepGraphActivity;
import com.fitkits.Analytics.Water.WaterGraphActivity;
import com.fitkits.Analytics.Weight.WeightGraphActivity;
import com.fitkits.CustomCalendarView.CaldroidFragment;
import com.fitkits.Model.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

  CardView weightCard,calorieCard,activityCard,sleepCard,waterCard,viewAttendance;
  TextView calories,weight,active,sleep,water,attended,totalThisMonth;
  Realm realmUI;
  LinearLayout content,viewFeed;
  ProgressDialog progressDialog;
  RecyclerView fitkits_news_recyclerView;
  SharedPreferences myPrefs;
  public HomeFragment() {
  }
  CustomViewPager banner;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.fragment_home, container, false);
    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.appbar);
    TextView day = (TextView) toolbar.findViewById(R.id.toolbar_day);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
    TextView date = (TextView) toolbar.findViewById(R.id.toolbar_date);
    TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
    weightCard = (CardView) view.findViewById(R.id.weightCard);
    calorieCard = (CardView) view.findViewById(R.id.calorieCard);
    activityCard = (CardView) view.findViewById(R.id.activityCard);
    sleepCard = (CardView) view.findViewById(R.id.sleepCard);
    waterCard = (CardView) view.findViewById(R.id.waterCard);
    content = (LinearLayout) view.findViewById(R.id.content);
    viewAttendance = (CardView) view.findViewById(R.id.viewAttendance);
    viewFeed = (LinearLayout) view.findViewById(R.id.viewFeed);
    fitkits_news_recyclerView = (RecyclerView) view.findViewById(R.id.fitkits_news_recycleview);
    fitkits_news_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    calories=(TextView)view.findViewById(R.id.calories);
    weight=(TextView)view.findViewById(R.id.weight);
    active=(TextView)view.findViewById(R.id.active);
    sleep=(TextView)view.findViewById(R.id.sleep);
    water=(TextView)view.findViewById(R.id.water);
    attended=(TextView)view.findViewById(R.id.attended);
    totalThisMonth=(TextView)view.findViewById(R.id.totalThisMonth);

    realmUI=Realm.getDefaultInstance();

    com.fitkits.Model.User userMasterGoal=realmUI.where(com.fitkits.Model.User.class).findFirst();


    if(userMasterGoal!=null) {
      calories.setText(String.valueOf(userMasterGoal.getGoals().getCaloriesPerDay().getValue()));
      weight.setText(String.valueOf(userMasterGoal.getGoals().getWeight().getValue()));
      active.setText(String.valueOf(userMasterGoal.getGoals().getActivePerDay().getValue()));
      sleep.setText(String.valueOf(userMasterGoal.getGoals().getSleepDurationPerDay().getValue()));
      water.setText(
          String.valueOf(userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue() / 250));
    }

    getAttendance();


    viewAttendance.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new CaldroidFragment()).commit();

        HomeActivity.home_select.setVisibility(View.INVISIBLE);
        HomeActivity.news_select.setVisibility(View.INVISIBLE);
        HomeActivity.att_select.setVisibility(View.VISIBLE);
        HomeActivity.profile_select.setVisibility(View.INVISIBLE);

      }
    });
    viewFeed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //startActivity(new Intent(HomeActivity.this,DummyActivity.class));
        HomeActivity.home_select.setVisibility(View.INVISIBLE);
        HomeActivity.news_select.setVisibility(View.VISIBLE);
        HomeActivity.att_select.setVisibility(View.INVISIBLE);
        HomeActivity. profile_select.setVisibility(View.INVISIBLE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new FitkitsNewsFragment()).commit();

      }
    });

    weightCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), WeightGraphActivity.class));
      }
    });

    calorieCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), CalorieGraphActivity.class));
      }
    });

    activityCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), ActiveHoursGraphActivity.class));
      }
    });

    sleepCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), SleepGraphActivity.class));
      }
    });

    waterCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), WaterGraphActivity.class));
      }
    });

    ImageView dropdown = (ImageView) toolbar.findViewById(R.id.toolbar_dropdown);
    date.setVisibility(View.VISIBLE);
    day.setVisibility(View.VISIBLE);
    dropdown.setVisibility(View.VISIBLE);
    title.setText("Attendance");
    title.setVisibility(View.GONE);
    banner=(CustomViewPager)view.findViewById(R.id.banner);
    banner.setAdapter(new BannerViewPagerAdapter(getChildFragmentManager(),getContext()));
    return view;
  }


  void getAttendance() {
    ApiService apiService= RetroClient.getApiService(myPrefs.getString("mobileNumber",""),myPrefs.getString("otp",""),getContext().getApplicationContext());


    apiService.getAttendance().subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ItemParent>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(ItemParent itemParent) {

            List<Attendance> value=itemParent.getAttendance();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date attendanceDate = new Date();
            Calendar calendar=Calendar.getInstance();
            Calendar calendarToday=Calendar.getInstance();


            int attendanceThisMonth=0;
            for(int i=0;i<value.size();i++){

              if(value.get(i).getStatus()==true) {
                try {
                  attendanceDate = sdf.parse(value.get(i).getModifiedAt());
                  calendar.setTime(attendanceDate);
                  if (calendar.get(Calendar.MONTH) == calendarToday
                      .get(Calendar.MONTH)) {
                    attendanceThisMonth = attendanceThisMonth + 1;
                  }

                } catch (ParseException e) {
                  e.printStackTrace();
                }
              }
            }
            attended.setText(String.valueOf(attendanceThisMonth));
            totalThisMonth.setText("/"+String.valueOf(calendarToday.getActualMaximum(Calendar.DAY_OF_MONTH)));
            loadBlogList(apiService);


          }

          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(homeActivity,"Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });


  }



  void loadBlogList(ApiService apiService) {

    apiService.getFeeds().subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ItemParent>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(ItemParent itemParent) {

            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }

            List<Feed> value=itemParent.getFeeds();


            if (value.size() > 0) {
              fitkits_news_recyclerView
                  .setAdapter(new FitkitsNewsShortAdapter(getActivity(), value));
            }
            content.setVisibility(View.VISIBLE);


          }

          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(homeActivity, "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });

  }
  }

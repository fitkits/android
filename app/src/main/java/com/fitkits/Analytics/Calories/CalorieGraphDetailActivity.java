package com.fitkits.Analytics.Calories;

import static java.security.AccessController.getContext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.ApiService;
import com.fitkits.Model.Aggregate;
import com.fitkits.R;
import com.fitkits.RetroClient;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalorieGraphDetailActivity extends AppCompatActivity {

ViewPager weightGraphViewPager;
TabLayout tabLayout;
ProgressDialog progressDialog;
SharedPreferences myPrefs;
public static TextView average,averageTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calorie_graph_detail);
    average=(TextView)findViewById(R.id.average);
    averageTitle=(TextView)findViewById(R.id.averageTitle);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    weightGraphViewPager=(ViewPager)findViewById(R.id.weightGraphViewPager);
    weightGraphViewPager.setOffscreenPageLimit(4);
    weightGraphViewPager.setAdapter(new CalorieGraphAdapter(getSupportFragmentManager()));
    tabLayout = (TabLayout)findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(weightGraphViewPager);
    ImageView back=(ImageView)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    weightGraphViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
      if(position==0){
        averageTitle.setText("Weekly Average");
        getCalorieAggregateWeekly();
      }
      else if(position==1){
        averageTitle.setText("Monthly Average");
        getCalorieAggregateMonthly();

      }
      else if(position==2){
        averageTitle.setText("Quarterly Average");
        getCalorieAggregateQuarterly();

      }
      else if(position==3){
        averageTitle.setText("Yearly Average");
        getCalorieAggregateYearly();
      }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });



  }


  void getCalorieAggregateWeekly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getCalorieAggregateMonthly(myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Aggregate>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(Aggregate value) {

            Calendar calendar=Calendar.getInstance();


            for(int i=0;i<value.getAggregate().getValue().size();i++){
              int week=0;
              if(calendar.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
                week=(calendar.get(Calendar.WEEK_OF_YEAR));
              }
              else{
                week=(calendar.get(Calendar.WEEK_OF_YEAR)-1);

              }
              if(week==value.getAggregate().getValue().get(i).getWeek()+1){
                average.setText(value.getAggregate().getValue().get(i).getAverage()+" calories consumed");
              }
            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(CalorieGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  void getCalorieAggregateMonthly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getCalorieAggregateQuarterly(myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Aggregate>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(Aggregate value) {

            Calendar calendar=Calendar.getInstance();

            for(int i=0;i<value.getAggregate().getValue().size();i++){
              if((calendar.get(Calendar.MONTH)+1)==value.getAggregate().getValue().get(i).getMonth()){
                average.setText(value.getAggregate().getValue().get(i).getAverage()+" calories consumed");
              }
            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(CalorieGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }


  void getCalorieAggregateQuarterly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getCalorieAggregateYearly(myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Aggregate>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(Aggregate value) {

            Calendar calendar=Calendar.getInstance();
            int month=(calendar.get(Calendar.MONTH)+1);

            for(int i=0;i<value.getAggregate().getValue().size();i++){
              if(month>=1&&month<=3) {
                if (value.getAggregate().getValue().get(i).getQuarter().equals("FIRST")) {
                  average.setText(value.getAggregate().getValue().get(i).getAverage()
                          + " calories consumed");
                }
              }
              else if(month>=4&&month<=6) {

                if (value.getAggregate().getValue().get(i).getQuarter().equals("SECOND")) {
                  average.setText(value.getAggregate().getValue().get(i).getAverage()
                          + " calories consumed");
                }
              }
              else if(month>=7&&month<=9) {
                if (value.getAggregate().getValue().get(i).getQuarter().equals("THIRD")) {
                  average.setText(value.getAggregate().getValue().get(i).getAverage()
                          + " calories consumed");
                }
              }
              else if(month>=10&&month<=12) {
                 if(value.getAggregate().getValue().get(i).getQuarter().equals("FOURTH")) {
                  average.setText(
                     value.getAggregate().getValue().get(i).getAverage()
                          + " calories consumed");
                }
              }

            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(CalorieGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  void getCalorieAggregateYearly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getCalorieAggregateAllYear(myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Aggregate>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(Aggregate value) {

            Calendar calendar = Calendar.getInstance();
            int month = (calendar.get(Calendar.MONTH) + 1);

            for (int i = 0; i < value.getAggregate().getValue().size(); i++) {
              if((calendar.get(Calendar.YEAR))==value.getAggregate().getValue().get(i).getYear()){
                average.setText(value.getAggregate().getValue().get(i).getAverage()+" calories consumed");
              }

            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(CalorieGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }




}

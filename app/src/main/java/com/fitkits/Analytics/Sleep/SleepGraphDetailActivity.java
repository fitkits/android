package com.fitkits.Analytics.Sleep;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Model.Aggregate;
import com.fitkits.R;
import com.fitkits.Misc.RetroClient;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;

public class SleepGraphDetailActivity extends AppCompatActivity {

ViewPager sleepGraphViewPager;
TabLayout tabLayout;
ProgressDialog progressDialog;
SharedPreferences myPrefs;
public static TextView average,averageTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sleep_graph_detail);
    average=(TextView)findViewById(R.id.average);
    averageTitle=(TextView)findViewById(R.id.averageTitle);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    sleepGraphViewPager =(ViewPager)findViewById(R.id.sleepGraphViewPager);
    sleepGraphViewPager.setOffscreenPageLimit(4);
    sleepGraphViewPager.setAdapter(new SleepGraphAdapter(getSupportFragmentManager()));
    tabLayout = (TabLayout)findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(sleepGraphViewPager);
    ImageView back=(ImageView)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    sleepGraphViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
      if(position==0){
        averageTitle.setText(R.string.TXT_WEEKLY_AVERAGE);
        getSleepAggregateWeekly();
      }
      else if(position==1){
        averageTitle.setText(R.string.TXT_MONTHLY_AVERAGE);
        getSleepAggregateMonthly();

      }
      else if(position==2){
        averageTitle.setText(R.string.TXT_QUARTERLY_AVERAGE);
        getSleepAggregateQuarterly();

      }
      else if(position==3){
        averageTitle.setText(R.string.TXT_YEARLY_AVERAGE);
        getSleepAggregateYearly();
      }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });



  }


  void getSleepAggregateWeekly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getSleepAggregateMonthly(myPrefs.getString("user_id","")).subscribeOn(
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
                average.setText(value.getAggregate().getValue().get(i).getAverage().toString()+R.string.TXT_HOURS);
              }
            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(SleepGraphDetailActivity.this,
                R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  void getSleepAggregateMonthly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getSleepAggregateQuarterly(myPrefs.getString("user_id","")).subscribeOn(
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
                average.setText(value.getAggregate().getValue().get(i).getAverage().toString()+R.string.TXT_HOURS);
              }
            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(SleepGraphDetailActivity.this,
                R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }


  void getSleepAggregateQuarterly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getSleepAggregateYearly(myPrefs.getString("user_id","")).subscribeOn(
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
                  average.setText(value.getAggregate().getValue().get(i).getAverage().toString()
                          + R.string.TXT_HOURS);
                }
              }
              else if(month>=4&&month<=6) {

                if (value.getAggregate().getValue().get(i).getQuarter().equals("SECOND")) {
                  average.setText(value.getAggregate().getValue().get(i).getAverage().toString()
                          + R.string.TXT_HOURS);
                }
              }
              else if(month>=7&&month<=9) {
                if (value.getAggregate().getValue().get(i).getQuarter().equals("THIRD")) {
                  average.setText(value.getAggregate().getValue().get(i).getAverage().toString()
                          + R.string.TXT_HOURS);
                }
              }
              else if(month>=10&&month<=12) {
                 if(value.getAggregate().getValue().get(i).getQuarter().equals("FOURTH")) {
                  average.setText(
                     value.getAggregate().getValue().get(i).getAverage().toString()
                          + R.string.TXT_HOURS);
                }
              }

            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(SleepGraphDetailActivity.this,
                R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  void getSleepAggregateYearly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getSleepAggregateAllYear(myPrefs.getString("user_id","")).subscribeOn(
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
                average.setText(value.getAggregate().getValue().get(i).getAverage().toString()+R.string.TXT_HOURS);
              }

            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(SleepGraphDetailActivity.this,
                    R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }




}

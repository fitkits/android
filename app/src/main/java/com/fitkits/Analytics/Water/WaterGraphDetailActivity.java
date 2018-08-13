package com.fitkits.Analytics.Water;

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
import com.fitkits.ApiService;
import com.fitkits.Model.Aggregate;
import com.fitkits.R;
import com.fitkits.RetroClient;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.text.DecimalFormat;
import java.util.Calendar;

public class WaterGraphDetailActivity extends AppCompatActivity {

ViewPager waterGraphViewPager;
TabLayout tabLayout;
ProgressDialog progressDialog;
SharedPreferences myPrefs;
public static TextView average,averageTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_water_graph_detail);
    average=(TextView)findViewById(R.id.average);
    averageTitle=(TextView)findViewById(R.id.averageTitle);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    waterGraphViewPager =(ViewPager)findViewById(R.id.waterGraphViewPager);
    waterGraphViewPager.setOffscreenPageLimit(4);
    waterGraphViewPager.setAdapter(new WaterGraphAdapter(getSupportFragmentManager()));
    tabLayout = (TabLayout)findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(waterGraphViewPager);
    ImageView back=(ImageView)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    waterGraphViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
      if(position==0){
        averageTitle.setText("Weekly Average");
        getWaterAggregateWeekly();
      }
      else if(position==1){
        averageTitle.setText("Monthly Average");
        getWaterAggregateMonthly();

      }
      else if(position==2){
        averageTitle.setText("Quarterly Average");
        getWaterAggregateQuarterly();

      }
      else if(position==3){
        averageTitle.setText("Yearly Average");
        getWaterAggregateYearly();
      }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });



  }


  void getWaterAggregateWeekly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getWaterAggregateMonthly(myPrefs.getString("user_id","")).subscribeOn(
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
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                average.setText(decimalFormat.format(value.getAggregate().getValue().get(i).getAverage()/250)+" glasses ");              }
            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(WaterGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  void getWaterAggregateMonthly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getWaterAggregateQuarterly(myPrefs.getString("user_id","")).subscribeOn(
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
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                average.setText(decimalFormat.format(value.getAggregate().getValue().get(i).getAverage()/250)+" glasses ");              }
            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(WaterGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }


  void getWaterAggregateQuarterly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getWaterAggregateYearly(myPrefs.getString("user_id","")).subscribeOn(
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
                  DecimalFormat decimalFormat = new DecimalFormat("#.00");
                  average.setText(decimalFormat.format(value.getAggregate().getValue().get(i).getAverage()/250)+" glasses ");
                }
              }
              else if(month>=4&&month<=6) {

                if (value.getAggregate().getValue().get(i).getQuarter().equals("SECOND")) {
                  DecimalFormat decimalFormat = new DecimalFormat("#.00");
                  average.setText(decimalFormat.format(value.getAggregate().getValue().get(i).getAverage()/250)+" glasses ");
                }
              }
              else if(month>=7&&month<=9) {
                if (value.getAggregate().getValue().get(i).getQuarter().equals("THIRD")) {
                  DecimalFormat decimalFormat = new DecimalFormat("#.00");
                  average.setText(decimalFormat.format(value.getAggregate().getValue().get(i).getAverage()/250)+" glasses ");
                }
              }
              else if(month>=10&&month<=12) {
                 if(value.getAggregate().getValue().get(i).getQuarter().equals("FOURTH")) {
                   DecimalFormat decimalFormat = new DecimalFormat("#.00");
                   average.setText(decimalFormat.format(value.getAggregate().getValue().get(i).getAverage()/250)+" glasses ");
                 }
              }

            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(WaterGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  void getWaterAggregateYearly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getApplicationContext());

    apiService.getWaterAggregateAllYear(myPrefs.getString("user_id","")).subscribeOn(
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
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                average.setText(decimalFormat.format(value.getAggregate().getValue().get(i).getAverage()/250)+" glasses ");
              }

            }


          }


          @Override
          public void onError(Throwable e) {

            Log.d("Response", e.getMessage());
            Toast.makeText(WaterGraphDetailActivity.this,
                "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }




}

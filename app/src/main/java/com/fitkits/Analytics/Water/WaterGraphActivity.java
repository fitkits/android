package com.fitkits.Analytics.Water;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Analytics.GoalData;
import com.fitkits.Analytics.GoalDataViewPagerAdapter;
import com.fitkits.ApiService;
import com.fitkits.CustomFixedHeightViewPager;
import com.fitkits.Answers.LogWaterDialog;
import com.fitkits.Answers.LogWaterDialog.InterfaceCommunicator;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.User;
import com.fitkits.Model.WeeklyData;
import com.fitkits.R;
import com.fitkits.RetroClient;
import com.fitkits.WaterDialog;
import com.fitkits.WaterGlassAdapter;
import com.fitkits.chart.tooltip.Tooltip;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WaterGraphActivity extends AppCompatActivity implements InterfaceCommunicator,WaterDialog.InterfaceCommunicator {






  Tooltip mTip;
  SharedPreferences myPrefs;
  ProgressDialog progressDialog;
  List<List<String>> weeksinMonth=new ArrayList<List<String>>();
  User userMasterGoal;
  public static WaterGraphActivity waterGraphActivity;

  CustomFixedHeightViewPager waterDataViewPager;
  RecyclerView glassCountRecylerview;
  TabLayout tabLayout;
  CardView logwater;

  ImageView setGoal;
  TextView target;

  private boolean mFirstStage;
  Realm realmUI;
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    realmUI=Realm.getDefaultInstance();
    setContentView(R.layout.activity_water_graph);
    waterGraphActivity=this;
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    glassCountRecylerview = (RecyclerView) findViewById(R.id.glassCountRecyclerView);
    glassCountRecylerview.setLayoutManager(new GridLayoutManager(this,4));
    setGoal=(ImageView)findViewById(R.id.setGoal);
    waterDataViewPager=(CustomFixedHeightViewPager)findViewById(R.id.weightDataViewPager);
    Log.d("user_id",myPrefs.getString("user_id",""));
    ImageView back=(ImageView)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    LinearLayout viewMore=(LinearLayout)findViewById(R.id.viewMore);
    viewMore.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(WaterGraphActivity.this,WaterGraphDetailActivity.class));
      }
    });
    target=(TextView)findViewById(R.id.target);
    tabLayout = (TabLayout)findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(waterDataViewPager);
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    logwater=(CardView)findViewById(R.id.logWater);
    logwater.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        LogWaterDialog logWaterDialog = new LogWaterDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "graph");
        logWaterDialog.setArguments(bundle);
        logWaterDialog.show(getSupportFragmentManager(), "Weight");
      }
    });
    setGoal.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        WaterDialog waterDialog = new WaterDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "graph");
        waterDialog.setArguments(bundle);
        waterDialog.show(getSupportFragmentManager(), "Weight");
      }
    });




    calcDates(0);


    // Data






  }
  void calcDates(int from){
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");



    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

    cal.set(Calendar.HOUR_OF_DAY,23);
    cal.set(Calendar.MINUTE,59);
    cal.set(Calendar.SECOND,00);
    cal.set(Calendar.MILLISECOND,000);
    Date endDate=cal.getTime();
    String endDateofThisWeek=format.format(cal.getTime());

    int currDayInt=cal.get(Calendar.DAY_OF_WEEK);
    if(currDayInt==1)
      currDayInt=currDayInt+5;
    else
      currDayInt=currDayInt-2;
    int month=cal.get(Calendar.MONTH);
    cal.add(Calendar.DATE,-currDayInt);
    if(cal.get(Calendar.MONTH)==month) {
      cal.set(Calendar.HOUR_OF_DAY, 00);
      cal.set(Calendar.MINUTE, 00);
      cal.set(Calendar.SECOND, 00);
      cal.set(Calendar.MILLISECOND, 000);
    }
    else {
      cal.add(Calendar.MONTH,1);
      cal.set(Calendar.DATE,01);
      cal.set(Calendar.HOUR_OF_DAY,00);
      cal.set(Calendar.MINUTE,00);
      cal.set(Calendar.SECOND,00);
      cal.set(Calendar.MILLISECOND,000);
    }
    Date startDate=cal.getTime();

    String startDateofThisWeek=format.format(cal.getTime());
    weeksinMonth=getNumberOfWeeks(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),startDateofThisWeek,endDateofThisWeek);
    for(int i=0;i<weeksinMonth.size();i++){
      Log.d("testweek",weeksinMonth.get(i).get(0)+"-"+weeksinMonth.get(i).get(1));
    }


    getWaterAggregate(startDate,endDate,weeksinMonth,from);

  }
  void getWaterAggregate(Date stDate,Date enDate,List<List<String>> weeksinMonth,int from) {
    ApiService apiService=RetroClient.getApiService(myPrefs.getString("mobileNumber",""),myPrefs.getString("otp",""),getApplicationContext());

    SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

    apiService.getWeeklyAggregate("/api/v1/cms/answers?start="+formatUTC.format(stDate)+"&end="+formatUTC.format(enDate)+"&user="+myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ItemParent>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(WaterGraphActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(ItemParent itemParent) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            List<WeeklyData> value=itemParent.getAnswers();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
            SimpleDateFormat sdf_new_rev = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat sdf_graph = new SimpleDateFormat("dd/MM/yyyy");

            Date startD =new Date();
            Date endD =new Date();
            String startDate=sdf_new_rev.format(stDate);
            String endDate=sdf_new_rev.format(enDate);
            try {
              startD = sdf_new_rev.parse(startDate);
              endD = sdf_new_rev.parse(endDate);
            }
            catch (ParseException e){

            }
                Calendar calendarStart=Calendar.getInstance();
                Calendar calendarEnd=Calendar.getInstance();
                calendarStart.setTime(startD);
                calendarEnd.setTime(endD);
            ArrayList<GoalData> datesList = new ArrayList<GoalData>();
            if (calendarStart.get(Calendar.DATE) == calendarEnd.get(Calendar.DATE)) {
              datesList.add(new GoalData(sdf_graph.format(calendarEnd.getTime()), 0));
            } else {
              while (calendarStart.get(Calendar.DATE) != calendarEnd.get(Calendar.DATE)) {
                Date curDate = calendarEnd.getTime();
                datesList.add(new GoalData(sdf_graph.format(curDate), 0));
                calendarEnd.add(Calendar.DATE, -1);
              }
              datesList.add(new GoalData(sdf_graph.format(calendarEnd.getTime()), 0));

            }

            float sumval=0;

            for (int i = 0; i < value.size(); i++) {

              if(value.get(i).getType().equals("waterConsumptionPerDay")) {
                for(int j=0;j<value.size();j++) {
                  if (value.get(j).getType().equals("waterConsumptionPerDay")) {

                    try {
                      if (sdf_graph.format(sdf.parse(value.get(i).getTimeStamp()))
                          .equals(sdf_graph.format(sdf.parse(value.get(j).getTimeStamp())))){
                        sumval = sumval + value.get(j).getValue();
                      }
                    } catch (ParseException e) {
                      e.printStackTrace();
                    }
                  }
                }
                try {
                  Date date = sdf.parse(value.get(i).getTimeStamp());
                  String dateGraphString = sdf_graph.format(date);

                  for (int j = 0; j < datesList.size(); j++) {
                    if (dateGraphString.equalsIgnoreCase(datesList.get(j).getDate())) {
                      datesList.get(j).setVal(
                          sumval);
                      sumval=0;
                    }
                  }


                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }

              Collections.reverse(datesList);
            for (int j = 0; j < datesList.size(); j++) {

              Log.d("dateList",datesList.get(j).getDate()+"->"+datesList.get(j).getVal());
            }


            realmUI.beginTransaction();
            userMasterGoal=realmUI.where(User.class).findFirst();
            realmUI.commitTransaction();

            for(int i=0;i<datesList.size();i++){
              if(i==datesList.size()-1) {
                if(datesList.get(i).getVal()!=0) {
                  if(userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()!=0) {
                    if (datesList.get(i).getVal()< userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()) {
                      float req =   userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()-datesList.get(i).getVal();
                      DecimalFormat decimalFormat = new DecimalFormat("#.00");

                      target.setText(decimalFormat.format(req/250) + " glasses to go!");

                      } else {
                        target.setText("You've reached your goal.");

                      }
                    }
                    else{
                      target.setText("Please set your goal.");

                    }
                  }
                  else{
                    target.setText("Please log today's water intake.");

                  }

                }
              }

Log.d("con","->"+datesList.get(datesList.size()-1).getVal());
                glassCountRecylerview.setAdapter(new WaterGlassAdapter(userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()/250,(int)datesList.get(datesList.size()-1).getVal()/250));


            waterDataViewPager.setAdapter(new GoalDataViewPagerAdapter(getSupportFragmentManager(),datesList,weeksinMonth,"waterConsumptionPerDay"));
            waterDataViewPager.setOffscreenPageLimit(weeksinMonth.size());



            }

            @Override
            public void onError(Throwable e) {
              if (progressDialog.isShowing() && this != null) {
                progressDialog.dismiss();
              }
              Log.d("Response", e.getMessage());
              Toast.makeText(WaterGraphActivity.this,
                  "Something went wrong. Please try again later.",
                  Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete() {

            }
          });

  }

  List<List<String>> getNumberOfWeeks(int year, int month,String startDate,String endDate) {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    List<List<String>> weekdates = new ArrayList<List<String>>();
    List<String> dates;
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, 1);
    Log.d("StartDAte",startDate);
    while ((c.get(Calendar.MONTH) == month)&&(!format.format(c.getTime()).equals(startDate))) {
        dates = new ArrayList<String>();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
          c.add(Calendar.DAY_OF_MONTH, -1);
        }
        if(c.get(Calendar.MONTH) == month) {
          dates.add(format.format(c.getTime()));
        }
        else{
          int actualDate=c.get(Calendar.DAY_OF_MONTH);
          int actualMonth=c.get(Calendar.MONTH);
          c.add(Calendar.MONTH,1);
          c.set(Calendar.DAY_OF_MONTH,c.getActualMinimum(Calendar.DAY_OF_MONTH));
          dates.add(format.format(c.getTime()));
          c.set(Calendar.DAY_OF_MONTH,actualDate);
          c.set(Calendar.MONTH,actualMonth);


        }
        c.add(Calendar.DAY_OF_MONTH, 6);
        if(c.get(Calendar.MONTH) == month) {
          dates.add(format.format(c.getTime()));
        }
        else{
          int actualDate=c.get(Calendar.DAY_OF_MONTH);
          int actualMonth=c.get(Calendar.MONTH);
          c.add(Calendar.MONTH,-1);
          c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
          dates.add(format.format(c.getTime()));
          c.set(Calendar.DAY_OF_MONTH,actualDate);
          c.set(Calendar.MONTH,actualMonth);
        }
        weekdates.add(dates);
        c.add(Calendar.DAY_OF_MONTH, 1);


    }
    dates = new ArrayList<String>();
    dates.add(startDate);
    dates.add(endDate);
    weekdates.add(dates);
    System.out.println(weekdates);

    return weekdates;
  }

  @Override
  public void refresh() {
    calcDates(1);
  }

  @Override
  public void refresh(int pos) {

  }

}

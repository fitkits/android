package com.fitkits.Analytics.Sleep;

import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Analytics.GoalData;
import com.fitkits.Analytics.GoalDataViewPagerAdapter;
import com.fitkits.Answers.LogSleepDialog;
import com.fitkits.ApiService;
import com.fitkits.CustomFixedHeightViewPager;
import com.fitkits.Answers.LogWeightDialog;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.SleepAnswer;
import com.fitkits.Model.User;
import com.fitkits.Model.WeeklyData;
import com.fitkits.R;
import com.fitkits.RetroClient;
import com.fitkits.SleepDialog;
import com.fitkits.WeightDialog;
import com.fitkits.chart.animation.Animation;
import com.fitkits.chart.model.BarSet;
import com.fitkits.chart.model.LineSet;
import com.fitkits.chart.renderer.AxisRenderer.LabelPosition;
import com.fitkits.chart.tooltip.Tooltip;
import com.fitkits.chart.util.Tools;
import com.fitkits.chart.view.StackBarChartView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SleepGraphActivity extends AppCompatActivity implements LogSleepDialog.InterfaceCommunicator,SleepDialog.InterfaceCommunicator {






  Tooltip mTip;
  SharedPreferences myPrefs;
  ProgressDialog progressDialog;
  int threshold=0;
  List<List<String>> weeksinMonth=new ArrayList<List<String>>();

  public static SleepGraphActivity sleepGraphActivity;

  CustomFixedHeightViewPager sleepDataViewPager;
  TabLayout tabLayout;
  CardView logweight;

  User userMasterGoal;
  ImageView setGoal;
  private StackBarChartView mChart;
  TextView target;
  SleepAnswer sleepAnswer;


  private boolean mFirstStage;
  Realm realmUI;
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    realmUI=Realm.getDefaultInstance();
    setContentView(R.layout.activity_sleep_graph);
    sleepGraphActivity=this;
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    mChart = (StackBarChartView)findViewById(R.id.chart_bar);
    setGoal=(ImageView)findViewById(R.id.setGoal);
    sleepDataViewPager =(CustomFixedHeightViewPager)findViewById(R.id.weightDataViewPager);
    target=(TextView)findViewById(R.id.target);
    tabLayout = (TabLayout)findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(sleepDataViewPager);
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    logweight=(CardView)findViewById(R.id.logWeight);
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
        startActivity(new Intent(SleepGraphActivity.this,SleepGraphDetailActivity.class));
      }
    });
    logweight.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
        sdf_new.setTimeZone(TimeZone.getTimeZone("IST"));

        Calendar cal = Calendar.getInstance();
        String current = sdf_new.format(cal.getTime());
        sleepAnswer = realmUI.where(SleepAnswer.class).equalTo("date", current).findFirst();
        if (sleepAnswer != null) {
          Toast.makeText(SleepGraphActivity.this,"You have already logged your sleep hours.",Toast.LENGTH_SHORT).show();

        } else {
          LogSleepDialog logSleepDialog = new LogSleepDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "graph");
          logSleepDialog.setArguments(bundle);
          logSleepDialog.show(getSupportFragmentManager(), "Sleep");
        }
      }
    });
    setGoal.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        SleepDialog sleepDialog = new SleepDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "graph");
        sleepDialog.setArguments(bundle);
        sleepDialog.show(getSupportFragmentManager(), "Sleep");
      }
    });








    mTip = new Tooltip(this, R.layout.linechart_three_tooltip, R.id.value);


    mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
    mTip.setDimensions(80,80);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

      mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
          PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
          PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

      mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
          PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
          PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

      mTip.setPivotX(Tools.fromDpToPx(65) / 2);
      mTip.setPivotY(Tools.fromDpToPx(25));

    }

    calcDates(0);

  }
  void calcDates(int from){
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");


    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

    cal.set(Calendar.HOUR_OF_DAY,00);
    cal.set(Calendar.MINUTE,00);
    cal.set(Calendar.SECOND,00);
    cal.set(Calendar.MILLISECOND,000);

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
      Log.d("nextdate",cal.getTime().toString());

    }
    else {

      cal.add(Calendar.MONTH,1);
      cal.set(Calendar.DATE,01);
      cal.set(Calendar.HOUR_OF_DAY,00);
      cal.set(Calendar.MINUTE,00);
      cal.set(Calendar.SECOND,00);
      cal.set(Calendar.MILLISECOND,000);
      Log.d("nextdate",cal.getTime().toString());

    }
    Date startDate=cal.getTime();

    String startDateofThisWeek=format.format(cal.getTime());

    month=cal.get(Calendar.MONTH);
    cal.add(Calendar.DATE,6);

    if(cal.get(Calendar.MONTH)==month) {
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 00);
      cal.set(Calendar.MILLISECOND, 000);
    }
    else {

      cal.add(Calendar.MONTH,-1);
      cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
      cal.set(Calendar.HOUR_OF_DAY,23);
      cal.set(Calendar.MINUTE,59);
      cal.set(Calendar.SECOND,00);
      cal.set(Calendar.MILLISECOND,000);

    }


    Date endDate=cal.getTime();
    weeksinMonth=getNumberOfWeeks(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),startDateofThisWeek,endDateofThisWeek);
    for(int i=0;i<weeksinMonth.size();i++) {
      Log.d("startDates", "=================");
      for (int j = 0; j < weeksinMonth.get(i).size(); j++) {
        Log.d("startDates", "=>" + weeksinMonth.get(i).get(j));
      }
    }

    getSleepHoursAggregate(startDate,endDate,weeksinMonth,from);

  }
  void getSleepHoursAggregate(Date stDate,Date enDate,List<List<String>> weeksinMonth,int from) {
    ApiService apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());

    SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

    apiService.getWeeklyAggregate("/api/v1/cms/answers?start="+formatUTC.format(stDate)+"&end="+formatUTC.format(enDate)+"&user="+myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
          new Observer<ItemParent>() {
            @Override
            public void onSubscribe(Disposable d) {
              progressDialog = new ProgressDialog(SleepGraphActivity.this);
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

              Paint thresPaint = new Paint();
              thresPaint.setColor(Color.parseColor("#90ff5c"));
              thresPaint.setPathEffect(new DashPathEffect(new float[]{25f, 10f}, 0));
              thresPaint.setStyle(Style.FILL);
              thresPaint.setAntiAlias(true);
              thresPaint.setStrokeWidth(Tools.fromDpToPx(1f));

              Paint axis = new Paint();
              axis.setColor(getResources().getColor(R.color.white_30));
              axis.setPathEffect(new PathEffect());
              axis.setStyle(Style.FILL);
              axis.setAntiAlias(true);
              axis.setStrokeWidth(Tools.fromDpToPx(.75f));

              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
              sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

              SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
              SimpleDateFormat sdf_new_rev = new SimpleDateFormat("yyyy-MM-dd");

              SimpleDateFormat sdf_graph = new SimpleDateFormat("dd/MM");

              Date startD =new Date();
              Date endD =new Date();

              String startDate=sdf_new_rev.format(stDate);
              String endDate=sdf_new_rev.format(enDate);
              try {
                startD = sdf_new_rev.parse(startDate);
                 endD = sdf_new_rev.parse(endDate);
                Log.d("stoend1",startDate+"-"+endDate);

                Log.d("stoend2",startD.toString()+"-"+endD.toString());
              }
              catch (ParseException e){

              }
              Calendar calendarStart = Calendar.getInstance();
              Calendar calendarEnd = Calendar.getInstance();
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

                if(value.get(i).getType().equals("sleepDurationPerDay")) {
                  for(int j=0;j<value.size();j++) {
                    if (value.get(j).getType().equals("sleepDurationPerDay")) {

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

              String[] mLabels = new String[datesList.size()];
              String[] defaultLabels = {"M","T","W","T","F","S","S"};

              float[] mValuesOne = new float[datesList.size()];
              realmUI.beginTransaction();
              userMasterGoal=realmUI.where(User.class).findFirst();
              realmUI.commitTransaction();
              float sum=0;
              for (int i = 0; i < datesList.size(); i++) {
                Log.d("theDates",datesList.get(i).getDate());
                sum=sum+datesList.get(i).getVal();
                if (i == datesList.size() - 1) {
                  if (datesList.get(i).getVal() != 0) {
                    if (userMasterGoal.getGoals().getCaloriesPerDay().getValue()!=0) {
                      if (datesList.get(i).getVal() > userMasterGoal.getGoals().getSleepDurationPerDay().getValue()) {
                        float req = datesList.get(i).getVal() - userMasterGoal.getGoals().getSleepDurationPerDay().getValue();
                        target.setText(req + " hours to go!");
                      } else {
                        target.setText("You've reached your goal.");

                      }
                    } else {
                      target.setText("Please set your goal.");

                    }
                  } else {
                    target.setText("Please log today's sleep hours.");

                  }

                }
                mLabels[i]=defaultLabels[i];

                mValuesOne[i] = (float)datesList.get(i).getVal();
              }
              if(sum>0.0f) {
                if (from == 0) {

                  BarSet stackBarSet = new BarSet(mLabels, mValuesOne);
                  stackBarSet.setColor(getResources().getColor(R.color.white_94));
                  mChart.addData(stackBarSet);
                  mChart.setXLabels(LabelPosition.TOP)
                      .setYLabels(LabelPosition.OUTSIDE)
                      .setValueThreshold(
                          (float) userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),
                          (float) userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),
                          thresPaint).setTooltips(mTip)
                      .show(new Animation().inSequence(.5f));
                  mChart.setStep(2);
                  mChart.setSetSpacing(10f);
                  mChart.setRoundCorners(10);
                  mChart.setAxisBorderValues(0f,24);
                  mChart.setLabelsColor(Color.WHITE);
                  mChart.setGrid(12,0,axis);
                  mChart.setYAxis(false);
                  mChart.setTypeface(
                      Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf"));
                } else {
                  mChart.reset();
                  BarSet stackBarSet = new BarSet(mLabels, mValuesOne);
                  stackBarSet.setColor(getResources().getColor(R.color.white_94));
                  mChart.addData(stackBarSet);

                  mChart.setXLabels(LabelPosition.TOP)
                      .setYLabels(LabelPosition.OUTSIDE)
                      .setValueThreshold(
                          (float) userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),
                          (float) userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),
                          thresPaint).setTooltips(mTip)
                      .show(new Animation().inSequence(.5f));
                  mChart.setStep(2);
                  mChart.setSetSpacing(10f);
                  mChart.setRoundCorners(10);
                  mChart.setAxisBorderValues(0f,24);
                  mChart.setLabelsColor(Color.WHITE);
                  mChart.setGrid(12,0,axis);
                  mChart.setYAxis(false);
                  mChart.setTypeface(
                      Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf"));
                }
              }
              sleepDataViewPager.setAdapter(
                  new GoalDataViewPagerAdapter(getSupportFragmentManager(), datesList, weeksinMonth,
                      "sleepDurationPerDay"));
              sleepDataViewPager.setOffscreenPageLimit(weeksinMonth.size());


            }

            @Override
            public void onError(Throwable e) {
              if (progressDialog.isShowing() && this != null) {
                progressDialog.dismiss();
              }
              Log.d("Response", e.getMessage());
              Toast.makeText(SleepGraphActivity.this,
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

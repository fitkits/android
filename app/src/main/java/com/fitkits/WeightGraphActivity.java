package com.fitkits;

import android.animation.PropertyValuesHolder;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Analytics.GoalData;
import com.fitkits.Analytics.GoalDataViewPagerAdapter;
import com.fitkits.Answers.LogWeightDialog;
import com.fitkits.Model.*;
import com.fitkits.Model.User;
import com.fitkits.chart.animation.Animation;
import com.fitkits.chart.model.LineSet;
import com.fitkits.chart.renderer.AxisRenderer.LabelPosition;
import com.fitkits.chart.renderer.YRenderer;
import com.fitkits.chart.tooltip.Tooltip;
import com.fitkits.chart.util.Tools;
import com.fitkits.chart.view.LineChartView;
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

public class WeightGraphActivity extends AppCompatActivity implements LogWeightDialog.InterfaceCommunicator,WeightDialog.InterfaceCommunicator {






  Tooltip mTip;
  SharedPreferences myPrefs;
  ProgressDialog progressDialog;
  int threshold=0;
  List<List<String>> weeksinMonth=new ArrayList<List<String>>();

  public static WeightGraphActivity weightGraphActivity;

  CustomFixedHeightViewPager weightDataViewPager;
  TabLayout tabLayout;
  CardView logweight;

  com.fitkits.Model.User userMasterGoal;
  ImageView setGoal;
  private LineChartView mChart;
  TextView target;

  private boolean mFirstStage;
  Realm realmUI;
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    realmUI=Realm.getDefaultInstance();
    setContentView(R.layout.activity_weight_graph);
    weightGraphActivity=this;
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    mChart = (LineChartView)findViewById(R.id.chart_Line);
    setGoal=(ImageView)findViewById(R.id.setGoal);
    weightDataViewPager=(CustomFixedHeightViewPager)findViewById(R.id.weightDataViewPager);
    target=(TextView)findViewById(R.id.target);
    tabLayout = (TabLayout)findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(weightDataViewPager);
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    logweight=(CardView)findViewById(R.id.logWeight);
    logweight.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        LogWeightDialog logWeightDialog = new LogWeightDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "graph");
        logWeightDialog.setArguments(bundle);
        logWeightDialog.show(getSupportFragmentManager(), "Weight");
      }
    });
    setGoal.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        WeightDialog weightDialog = new WeightDialog();
        Bundle bundle = new Bundle();
        bundle.putString("type", "graph");
        weightDialog.setArguments(bundle);
        weightDialog.show(getSupportFragmentManager(), "Weight");
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


    // Data






  }
  void calcDates(int from){
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd");



    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

    cal.set(Calendar.HOUR,11);
    cal.set(Calendar.MINUTE,59);
    cal.set(Calendar.SECOND,00);
    cal.set(Calendar.MILLISECOND,000);
    String endDate=formatNew.format(cal.getTime());
    String endDateofThisWeek=format.format(cal.getTime());

    int currDayInt=cal.get(Calendar.DAY_OF_WEEK);
    if(currDayInt==1)
      currDayInt=currDayInt+5;
    else
      currDayInt=currDayInt-2;
    int month=cal.get(Calendar.MONTH);
    cal.add(Calendar.DATE,-currDayInt);
    if(cal.get(Calendar.MONTH)==month) {
      cal.set(Calendar.HOUR, -12);
      cal.set(Calendar.MINUTE, 00);
      cal.set(Calendar.SECOND, 00);
      cal.set(Calendar.MILLISECOND, 000);
    }
    else {
      cal.add(Calendar.MONTH,1);
      cal.set(Calendar.DATE,01);
      cal.set(Calendar.HOUR,-12);
      cal.set(Calendar.MINUTE,00);
      cal.set(Calendar.SECOND,00);
      cal.set(Calendar.MILLISECOND,000);
    }
    String startDate=formatNew.format(cal.getTime());

    String startDateofThisWeek=format.format(cal.getTime());
    weeksinMonth=getNumberOfWeeks(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),startDateofThisWeek,endDateofThisWeek);

    for(int i=0;i<weeksinMonth.size();i++){
      Log.d("startDates","=================");
      for(int j=0;j<weeksinMonth.get(i).size();j++){
        Log.d("startDates","=>"+weeksinMonth.get(i).get(j));
      }
    }
    getWeightAggregate(startDate,endDate,weeksinMonth,from);

  }
  void getWeightAggregate(String  startDate,String endDate,List<List<String>> weeksinMonth,int from) {
    ApiService apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());

      apiService.getWeeklyAggregate("/api/v1/cms/answers?startTime="+startDate+"&endTime="+endDate).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
          new Observer<ItemParent>() {
            @Override
            public void onSubscribe(Disposable d) {
              progressDialog = new ProgressDialog(WeightGraphActivity.this);
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

              SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd'T'hh:mm:ss.SSS'Z'");
              SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
              SimpleDateFormat sdf_new_rev = new SimpleDateFormat("yyyy-MM-dd");

              SimpleDateFormat sdf_graph = new SimpleDateFormat("dd/MM");

              Date startD =new Date();
              Date endD =new Date();

              try {
                startD = sdf_new_rev.parse(startDate);
                 endD = sdf_new_rev.parse(endDate);
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

              for (int i = 0; i < value.size(); i++) {
                if(value.get(i).getType().equals("weight")) {
                  try {
                    Date date = sdf.parse(value.get(i).getTimeStamp());
                    String dateGraphString = sdf_graph.format(date);

                    for (int j = 0; j < datesList.size(); j++) {
                      if (dateGraphString.equalsIgnoreCase(datesList.get(j).getDate())) {
                        datesList.get(j).setVal(
                            value.get(i).getValue());
                      }
                    }


                  } catch (Exception e) {
                    e.printStackTrace();
                  }
                }
              }

              Collections.reverse(datesList);

              String[] mLabels = new String[datesList.size()];
              float[] mValuesOne = new float[datesList.size()];
              realmUI.beginTransaction();
              userMasterGoal=realmUI.where(User.class).findFirst();
              realmUI.commitTransaction();
              for (int i = 0; i < datesList.size(); i++) {
                if (i == datesList.size() - 1) {
                  mLabels[i] = "Today";
                  if (datesList.get(i).getVal() != 0) {
                    if (userMasterGoal.getGoals().getWeight().getValue()!=0) {
                      if (datesList.get(i).getVal() > userMasterGoal.getGoals().getWeight().getValue()) {
                        float req = datesList.get(i).getVal() - userMasterGoal.getGoals().getWeight().getValue();
                        target.setText(req + "Kgs to go!");
                      } else {
                        target.setText("You've reached your goal.");

                      }
                    } else {
                      target.setText("Please set your goal.");

                    }
                  } else {
                    target.setText("Please Log today's weight.");

                  }

                } else if (i == datesList.size() - 2) {
                  mLabels[i] = "Yesterday";
                } else {
                  mLabels[i] = datesList.get(i).getDate();
                }
                mValuesOne[i] = (float)datesList.get(i).getVal();
              }
              if (from == 0) {
                LineSet dataset = new LineSet(mLabels, mValuesOne);
                dataset.setColor(Color.WHITE)
                    .setDotsColor(Color.parseColor("#90ff5c"))
                    .setThickness(4)
                    .setDashed(new float[]{25f, 10f})
                    .beginAt(0);

                mChart.addData(dataset);
                mChart.setXLabels(LabelPosition.TOP)
                    .setYLabels(YRenderer.LabelPosition.OUTSIDE)
                    .setValueThreshold(
                        (float)userMasterGoal.getGoals().getWeight().getValue(),
                        (float)userMasterGoal.getGoals().getWeight().getValue(),
                        thresPaint).setTooltips(mTip)
                    .show(new Animation().inSequence(.5f));
                mChart.setStep(30);
                mChart.setAxisBorderValues(0f, 300f);
                mChart.setLabelsColor(Color.WHITE);
                mChart.setGrid(10, 0, axis);
                mChart.setYAxis(false);
                mChart.setTypeface(
                    Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf"));
              } else {
                mChart.reset();
                LineSet dataset = new LineSet(mLabels, mValuesOne);
                dataset.setColor(Color.WHITE)
                    .setDotsColor(Color.parseColor("#90ff5c"))
                    .setThickness(4)
                    .setDashed(new float[]{25f, 10f})
                    .beginAt(0);

                mChart.addData(dataset);

                mChart.setXLabels(LabelPosition.TOP)
                    .setYLabels(YRenderer.LabelPosition.OUTSIDE)
                    .setValueThreshold(
                        (float)userMasterGoal.getGoals().getWeight().getValue(),
                        (float)userMasterGoal.getGoals().getWeight().getValue(),
                        thresPaint).setTooltips(mTip)
                    .show(new Animation().inSequence(.5f));
                mChart.setStep(30);
                mChart.setAxisBorderValues(0f, 120f);
                mChart.setLabelsColor(Color.WHITE);
                mChart.setGrid(3, 0, axis);
                mChart.setYAxis(false);
                mChart.setTypeface(
                    Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf"));
              }







                /*Calendar calendar=Calendar.getInstance();
                Date curDate=calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                Date yesDate=calendar.getTime();
                String curdataeString=sdf_new.format(curDate);
                String yesdataeString=sdf_new.format(yesDate);

               ArrayList<WeightData> datesList=new ArrayList<WeightData>();
                int today=0,yesterday=0;
                for(int i=0;i<value.size();i++){
                  try {
                    Date date=sdf.parse(value.get(i).getModifiedAt());
                    String dateString=sdf_new.format(date);
                    String dateGraphString=sdf_graph.format(date);
                    if(dateString.equalsIgnoreCase(curdataeString)){

                      datesList.add(new WeightData("Today",value.get(i).getResponses().getData().getCurrentWeight().getValue().toString()));
                      today=1;

                    }
                    else if(dateString.equalsIgnoreCase(yesdataeString)){

                      datesList.add(new WeightData("Yesterday",value.get(i).getResponses().getData().getCurrentWeight().getValue().toString()));
                      yesterday=1;
                    }
                    else{
                      datesList.add(new WeightData(dateGraphString,value.get(i).getResponses().getData().getCurrentWeight().getValue().toString()));

                    }



                  } catch (Exception e) {
                    e.printStackTrace();
                  }

                }
                if(today==0&&yesterday==0){
                  datesList.add(new WeightData("Yesterday","0"));
                  datesList.add(new WeightData("Today","0"));

                }
                if(today==0&&yesterday==1){
                  datesList.add(new WeightData("Today","0"));
                }
                if(today==1&&yesterday==0){
                  WeightData todayData=datesList.get(datesList.size()-1);
                  datesList.remove(datesList.size()-1);
                  datesList.add(new WeightData("Yesterday","0"));
                  datesList.add(todayData);

                }

                String [] mLabels = new String[datesList.size()];
                float [] mValuesOne = new float[datesList.size()];

                for(int i=0;i<datesList.size();i++){
                  mLabels[i]=datesList.get(i).getDate();
                  mValuesOne[i]=Float.parseFloat(datesList.get(i).getWeightVal());
                }
                LineSet dataset = new LineSet(mLabels, mValuesOne);
                dataset.setColor(Color.WHITE)
                    .setDotsColor(Color.parseColor("#90ff5c"))
                    .setThickness(4)
                    .setDashed(new float[]{25f, 10f})
                    .beginAt(0);

                mChart.addData(dataset);*/

              weightDataViewPager.setAdapter(
                  new GoalDataViewPagerAdapter(getSupportFragmentManager(), datesList, weeksinMonth,
                      "weight"));
              weightDataViewPager.setOffscreenPageLimit(weeksinMonth.size());


             /* else{
                ArrayList<WeightData> datesList=new ArrayList<WeightData>();
                datesList.add(new WeightData("Today","0"));
                String [] mLabels = {"Today"};
                float [] mValuesOne = {0};
                LineSet dataset = new LineSet(mLabels, mValuesOne);
                dataset.setColor(Color.WHITE)
                    .setDotsColor(Color.parseColor("#90ff5c"))
                    .setThickness(4)
                    .setDashed(new float[]{25f, 10f})
                    .beginAt(0);

                mChart.addData(dataset);
                weightDataViewPager.setAdapter(new GoalDataViewPagerAdapter(getSupportFragmentManager(),datesList,weeksinMonth));


              }*/



            }

            @Override
            public void onError(Throwable e) {
              if (progressDialog.isShowing() && this != null) {
                progressDialog.dismiss();
              }
              Log.d("Response", e.getMessage());
              Toast.makeText(WeightGraphActivity.this,
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

package com.fitkits.Analytics.Sleep;

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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fitkits.Analytics.GoalData;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Model.Aggregate;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.Monthly;
import com.fitkits.Model.Quarterly;
import com.fitkits.Model.User;
import com.fitkits.Model.WeeklyData;
import com.fitkits.Model.Yearly;
import com.fitkits.R;
import com.fitkits.Misc.RetroClient;
import com.fitkits.chart.animation.Animation;
import com.fitkits.chart.model.BarSet;
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

public class SleepGraphFragment extends Fragment {



    SharedPreferences myPrefs;

    private StackBarChartView mChart;

    private boolean mFirstStage;

    ProgressDialog progressDialog;

     List<Monthly> weeksinMonth = new ArrayList<Monthly>();
   List<Quarterly> months = new ArrayList<Quarterly>();
   List<Yearly> quarters = new ArrayList<Yearly>();
  User userMasterGoal;
  Realm realmUI;
  Tooltip mTip;



  Bundle bundle;
    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_bar_graph, container, false);
        mChart = (StackBarChartView) layout.findViewById(R.id.chart);
      mChart.setBackground(getResources().getDrawable(R.drawable.gradient_chart_violet_bg));

      mFirstStage = true;
        realmUI=Realm.getDefaultInstance();
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
       mTip = new Tooltip(getContext(), R.layout.linechart_three_tooltip,
          R.id.value);

      ((TextView) mTip.findViewById(R.id.value)).setTypeface(
          Typeface
              .createFromAsset(getContext().getAssets(), "OpenSans-Semibold.ttf"));

      mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
      mTip.setMargins(0, 0, 0, 10);
      mTip.setDimensions(80, 80);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

        mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(300);

        mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

        mTip.setPivotX(Tools.fromDpToPx(65) / 2);
        mTip.setPivotY(Tools.fromDpToPx(25));
      }
        /*LineSet dataset = new LineSet(mLabels, mValuesOne);
        dataset.setColor(Color.parseColor("#004f7f"))
            .setThickness(Tools.fromDpToPx(3))
            .setSmooth(true)
            .beginAt(0)
            .endAt(2);

        mChart.addData(dataset);*/
      bundle=getArguments();
      pos=bundle.getInt("position");
        if(pos==0){
          calcDates();
        }
        if(pos==1) {
          getSleepAggregateMonthly();
        }
        if(pos==2){
          getSleepAggregateQuaterly();


        }
        if(pos==3){
          getSleepAggregateYearly();
        }








        return layout;
    }
  void calcDates(){
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


    getSleepAggregateWeekly(startDate,endDate);

  }



  List<Monthly> getNumberOfWeeks(int year, int month) {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    List<Monthly> weekdates = new ArrayList<Monthly>();
    List<String> dates;

    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, 1);
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

    while ((c.get(Calendar.MONTH) == month)&&(!c.getTime().toString().equals(cal.getTime().toString()))){
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
      weekdates.add(new Monthly(dates,0));
      c.add(Calendar.DAY_OF_MONTH, 1);


    }


    return weekdates;
  }


    List<Quarterly> getNumberOfMonths(int year, int month) {
        SimpleDateFormat format = new SimpleDateFormat("MMM");
      SimpleDateFormat formatYear = new SimpleDateFormat("yy");

      List<Quarterly> months = new ArrayList<Quarterly>();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
int i=3;
        while ((c.get(Calendar.YEAR) == year)&&i!=0){

                months.add(new Quarterly(format.format(c.getTime())+" '"+formatYear.format(c.getTime()),0));
                c.add(Calendar.MONTH,-1);

          i--;

        }

      Collections.reverse(months);
        return months;
    }


  List<Yearly> getNumberOfQuarters(int year, int month) {
    SimpleDateFormat format = new SimpleDateFormat("MMM");
    SimpleDateFormat formatYear = new SimpleDateFormat("yy");

    List<Yearly> quarters = new ArrayList<Yearly>();

    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, 0);
    c.set(Calendar.DAY_OF_MONTH, 1);
    while ((c.get(Calendar.YEAR) == year)){

      String startMonth=format.format(c.getTime());
      c.add(Calendar.MONTH,+2);
      String endMonth=format.format(c.getTime());

      quarters.add(new Yearly(startMonth+" - "+endMonth+" '"+formatYear.format(c.getTime()),0));
      c.add(Calendar.MONTH,+1);

    }
    return quarters;
  }


    void getSleepAggregateMonthly() {
        ApiService apiService = RetroClient
            .getApiService(myPrefs.getString("token", ""), getContext().getApplicationContext());

        apiService.getSleepAggregateMonthly(myPrefs.getString("user_id","")).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Observer<Aggregate>() {
                @Override
                public void onSubscribe(Disposable d) {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                }

                @Override
                public void onNext(Aggregate value) {
                    if (progressDialog.isShowing() && this != null) {
                        progressDialog.dismiss();
                    }
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
                      SleepGraphDetailActivity.average.setText(value.getAggregate().getValue().get(i).getAverage()+" hours");
                    }
                  }
                     calendar=Calendar.getInstance();
                    weeksinMonth=getNumberOfWeeks(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat format_new_half = new SimpleDateFormat("dd");
                    SimpleDateFormat format_new_full = new SimpleDateFormat("dd\nMMM");

                    String [] mLabels=new String[weeksinMonth.size()];
                    float [] mValuesOne=new float[weeksinMonth.size()];

for(int j=0;j<value.getAggregate().getValue().size();j++){
  Log.d("weekofm",String.valueOf(calendar.get(Calendar.MONTH)));

  if(calendar.get(Calendar.MONTH)==value.getAggregate().getValue().get(j).getMonth()-1){

      calendar.set(2018, 1, 1);

      calendar.set(Calendar.WEEK_OF_YEAR, (value.getAggregate().getValue().get(j).getWeek()+1));
        weeksinMonth.get(calendar.get(Calendar.WEEK_OF_MONTH)-1).setAverage((value.getAggregate().getValue().get(j).getAverage().floatValue()));


  }
}



float sum=0;
                for(int i=0;i<weeksinMonth.size();i++){
                        try {

                            Date startDate=format.parse(weeksinMonth.get(i).getDates().get(0));
                            Date endDate=format.parse(weeksinMonth.get(i).getDates().get(1));

                            mLabels[i]=format_new_half.format(startDate)+"-"+format_new_full.format(endDate).toUpperCase();
                            sum=sum+weeksinMonth.get(i).getAverage();
                            mValuesOne[i]=weeksinMonth.get(i).getAverage();
                          Log.d("sdate",format_new_half.format(startDate)+"-"+format_new_full.format(endDate).toUpperCase()+"->"+ weeksinMonth.get(i).getAverage());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if(sum>0.0f) {

                      Paint thresPaint = new Paint();
                      thresPaint.setColor(Color.parseColor("#90ff5c"));
                      thresPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                      thresPaint.setStyle(Style.FILL);
                      thresPaint.setAntiAlias(true);
                      thresPaint.setStrokeWidth(Tools.fromDpToPx(2f));

                      Paint axis = new Paint();
                      axis.setColor(getResources().getColor(R.color.white_30));
                      axis.setPathEffect(new PathEffect());
                      axis.setStyle(Style.FILL);
                      axis.setAntiAlias(true);
                      axis.setStrokeWidth(Tools.fromDpToPx(.75f));

                      Tooltip mTip = new Tooltip(getContext(), R.layout.linechart_three_tooltip,
                          R.id.value);

                      ((TextView) mTip.findViewById(R.id.value)).setTypeface(
                          Typeface
                              .createFromAsset(getContext().getAssets(), "OpenSans-Semibold.ttf"));

                      mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
                      mTip.setMargins(0, 0, 0, 10);
                      mTip.setDimensions(80, 80);

                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                        mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(300);

                        mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                            PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

                        mTip.setPivotX(Tools.fromDpToPx(65) / 2);
                        mTip.setPivotY(Tools.fromDpToPx(25));
                      }

                      BarSet stackBarSet = new BarSet(mLabels, mValuesOne);
                      stackBarSet.setColor(getResources().getColor(R.color.white_94));

                      mChart.addData(stackBarSet);
                      mChart.setAxisBorderValues(0f, 220);
                      realmUI.beginTransaction();
                      userMasterGoal = realmUI.where(User.class).findFirst();
                      realmUI.commitTransaction();
                      mChart.setXLabels(LabelPosition.OUTSIDE)
                          .setYLabels(LabelPosition.OUTSIDE)
                          .setValueThreshold((float)userMasterGoal.getGoals().getSleepDurationPerDay().getValue(), (float)userMasterGoal.getGoals().getSleepDurationPerDay().getValue(), thresPaint).setTooltips(mTip)
                          .show(new Animation().inSequence(.5f));
                      Log.d("threshold","->"+userMasterGoal.getGoals().getSleepDurationPerDay().getValue());
                      mChart.setStep(1);
                      mChart.setSetSpacing(10f);
                      mChart.setRoundCorners(10);
                      mChart.setAxisBorderValues(0f,24);
                      mChart.setLabelsColor(Color.WHITE);
                      mChart.setGrid(24,0,axis);
                      mChart.setYAxis(false);
                      mChart.setBarSpacing(60f);
                      mChart.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Semibold.ttf"));

                    }
                }


                @Override
                public void onError(Throwable e) {
                    if (progressDialog.isShowing() && this != null) {
                        progressDialog.dismiss();
                    }
                    Log.d("Response", e.getMessage());
                    Toast.makeText(getContext(),
                            R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                        Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete() {

                }
            });
    }


  void getSleepAggregateQuaterly() {
    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""), getContext().getApplicationContext());

    apiService.getSleepAggregateQuarterly(myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Aggregate>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(Aggregate value) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Calendar calendar=Calendar.getInstance();
            months=getNumberOfMonths(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));

            String [] mLabels=new String[months.size()];
            float [] mValuesOne=new float[months.size()];

            SimpleDateFormat format = new SimpleDateFormat("MMM");
            SimpleDateFormat formatYear = new SimpleDateFormat("yy");


            for(int j=0;j<value.getAggregate().getValue().size();j++){
              Calendar c = Calendar.getInstance();
              c.set(Calendar.YEAR, value.getAggregate().getValue().get(j).getYear());
              c.set(Calendar.MONTH, value.getAggregate().getValue().get(j).getMonth()-1);
              c.set(Calendar.DAY_OF_MONTH, 1);
              for(int i=0;i<months.size();i++){
                Log.d("frmat","=>"+months.get(i).getMonth()+format.format(c.getTime())+" '"+formatYear.format(c.getTime()));
                if(months.get(i).getMonth().equals(format.format(c.getTime())+" '"+formatYear.format(c.getTime()))){
                  months.get(i).setAverage((value.getAggregate().getValue().get(j).getAverage().floatValue()));

                }
              }
            }
            float sum=0;
            for(int i=0;i<months.size();i++){
                Log.d("Months",months.get(i).getMonth()+"->"+months.get(i).getAverage());
                mLabels[i]=months.get(i).getMonth();
                sum=sum+months.get(i).getAverage();
                mValuesOne[i]=months.get(i).getAverage();

            }

            if(sum>0.0f) {

              Paint thresPaint = new Paint();
              thresPaint.setColor(Color.parseColor("#90ff5c"));
              thresPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
              thresPaint.setStyle(Style.FILL);
              thresPaint.setAntiAlias(true);
              thresPaint.setStrokeWidth(Tools.fromDpToPx(2f));

              Paint axis = new Paint();
              axis.setColor(getResources().getColor(R.color.white_30));
              axis.setPathEffect(new PathEffect());
              axis.setStyle(Style.FILL);
              axis.setAntiAlias(true);
              axis.setStrokeWidth(Tools.fromDpToPx(.75f));

              Tooltip mTip = new Tooltip(getContext(), R.layout.linechart_three_tooltip,
                  R.id.value);

              ((TextView) mTip.findViewById(R.id.value)).setTypeface(
                  Typeface
                      .createFromAsset(getContext().getAssets(), "OpenSans-Semibold.ttf"));

              mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
              mTip.setMargins(0, 0, 0, 10);
              mTip.setDimensions(80, 80);

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(300);

                mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

                mTip.setPivotX(Tools.fromDpToPx(65) / 2);
                mTip.setPivotY(Tools.fromDpToPx(25));
              }

              BarSet stackBarSet = new BarSet(mLabels, mValuesOne);
              stackBarSet.setColor(getResources().getColor(R.color.white_94));

              mChart.addData(stackBarSet);
              mChart.setAxisBorderValues(0f, 220);
              realmUI.beginTransaction();
              userMasterGoal = realmUI.where(User.class).findFirst();
              realmUI.commitTransaction();
              mChart.setXLabels(LabelPosition.OUTSIDE)
                  .setYLabels(LabelPosition.OUTSIDE)
                  .setValueThreshold((float)userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),(float)userMasterGoal.getGoals().getSleepDurationPerDay().getValue(), thresPaint).setTooltips(mTip)
                  .show(new Animation().inSequence(.5f));
              mChart.setStep(1);
              mChart.setSetSpacing(10f);
              mChart.setRoundCorners(10);
              mChart.setAxisBorderValues(0f,24);
              mChart.setLabelsColor(Color.WHITE);
              mChart.setGrid(24,0,axis);
              mChart.setYAxis(false);
              mChart.setBarSpacing(60f);
              mChart.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Semibold.ttf"));

            }
          }


          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(getContext(),
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
        .getApiService(myPrefs.getString("token", ""), getContext().getApplicationContext());

    apiService.getSleepAggregateYearly(myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Aggregate>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(Aggregate value) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }

            Calendar calendar = Calendar.getInstance();
            quarters = getNumberOfQuarters(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH));

            String[] mLabels = new String[quarters.size()];
            float[] mValuesOne = new float[quarters.size()];

            for (int j = 0; j < value.getAggregate().getValue().size(); j++) {

              if (value.getAggregate().getValue().get(j).getQuarter().equals("FIRST")) {
                quarters.get(0)
                    .setAverage((value.getAggregate().getValue().get(j).getAverage().floatValue()));
              } else if (value.getAggregate().getValue().get(j).getQuarter().equals("SECOND")) {
                quarters.get(1)
                    .setAverage((value.getAggregate().getValue().get(j).getAverage().floatValue()));
              } else if (value.getAggregate().getValue().get(j).getQuarter().equals("THIRD")) {
                quarters.get(2)
                    .setAverage((value.getAggregate().getValue().get(j).getAverage().floatValue()));
              } else if (value.getAggregate().getValue().get(j).getQuarter().equals("FOURTH")) {
                quarters.get(3)
                    .setAverage((value.getAggregate().getValue().get(j).getAverage().floatValue()));
              }
            }
            float sum = 0;
            for (int i = 0; i < quarters.size(); i++) {
              mLabels[i] = quarters.get(i).getQuarter();
              sum = sum + quarters.get(i).getAverage();
              mValuesOne[i] = quarters.get(i).getAverage();

            }

            if (sum > 0.0f) {

              Paint thresPaint = new Paint();
              thresPaint.setColor(Color.parseColor("#90ff5c"));
              thresPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
              thresPaint.setStyle(Style.FILL);
              thresPaint.setAntiAlias(true);
              thresPaint.setStrokeWidth(Tools.fromDpToPx(2f));

              Paint axis = new Paint();
              axis.setColor(getResources().getColor(R.color.white_30));
              axis.setPathEffect(new PathEffect());
              axis.setStyle(Style.FILL);
              axis.setAntiAlias(true);
              axis.setStrokeWidth(Tools.fromDpToPx(.75f));



              BarSet stackBarSet = new BarSet(mLabels, mValuesOne);
              stackBarSet.setColor(getResources().getColor(R.color.white_94));

              mChart.addData(stackBarSet);
              mChart.setAxisBorderValues(0f, 220);
              realmUI.beginTransaction();
              userMasterGoal = realmUI.where(User.class).findFirst();
              realmUI.commitTransaction();
              mChart.setXLabels(LabelPosition.OUTSIDE)
                  .setYLabels(LabelPosition.OUTSIDE)
                  .setValueThreshold((float)userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),(float)userMasterGoal.getGoals().getSleepDurationPerDay().getValue(), thresPaint).setTooltips(mTip)
                  .show(new Animation().inSequence(.5f));
              mChart.setStep(1);
              mChart.setSetSpacing(10f);
              mChart.setRoundCorners(10);
              mChart.setAxisBorderValues(0f,24);
              mChart.setLabelsColor(Color.WHITE);
              mChart.setGrid(24,0,axis);
              mChart.setYAxis(false);
              mChart.setBarSpacing(60f);
              mChart.setTypeface(
                  Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Semibold.ttf"));

            }
          }


          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(getContext(),
                    R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  void getSleepAggregateWeekly(Date  stDate,Date enDate) {
    ApiService apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getContext().getApplicationContext());

    SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

    apiService.getWeeklyAggregate("/api/v1/cms/answers?start="+formatUTC.format(stDate)+"&end="+formatUTC.format(enDate)+"&user="+myPrefs.getString("user_id","")).subscribeOn(
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
            SimpleDateFormat sdf_new_rev = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat sdf_graph = new SimpleDateFormat("dd/MM");

            Date startD = new Date();
            Date endD = new Date();
            String startDate=sdf_new_rev.format(stDate);
            String endDate=sdf_new_rev.format(enDate);
            try {
              startD = sdf_new_rev.parse(startDate);
              endD = sdf_new_rev.parse(endDate);
            } catch (ParseException e) {

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
            float sumval = 0;

            for (int i = 0; i < value.size(); i++) {

              if (value.get(i).getType().equals("sleepDurationPerDay")) {
                for (int j = 0; j < value.size(); j++) {
                  if (value.get(j).getType().equals("sleepDurationPerDay")) {

                    try {
                      if (sdf_graph.format(sdf.parse(value.get(i).getTimeStamp()))
                          .equals(sdf_graph.format(sdf.parse(value.get(j).getTimeStamp())))) {
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
                      sumval = 0;
                    }
                  }


                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }

            Collections.reverse(datesList);

            String[] mLabels = new String[datesList.size()];
            String[] defaultLabels = {"M", "T", "W", "T", "F", "S", "S"};

            float[] mValuesOne = new float[datesList.size()];
            realmUI.beginTransaction();
            userMasterGoal = realmUI.where(User.class).findFirst();
            realmUI.commitTransaction();
            float sum = 0;
            for (int i = 0; i < datesList.size(); i++) {
              sum = sum + datesList.get(i).getVal();
              mLabels[i] = defaultLabels[i];

              mValuesOne[i] = (float) datesList.get(i).getVal();
            }
            if (sum > 0.0f) {
              BarSet stackBarSet = new BarSet(mLabels, mValuesOne);
              stackBarSet.setColor(getResources().getColor(R.color.white_94));
              mChart.addData(stackBarSet);
              mChart.setXLabels(LabelPosition.OUTSIDE)
                  .setYLabels(LabelPosition.OUTSIDE)
                  .setValueThreshold(
                      (float) userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),
                      (float) userMasterGoal.getGoals().getSleepDurationPerDay().getValue(),
                      thresPaint).setTooltips(mTip)
                  .show(new Animation().inSequence(.5f));
              mChart.setStep(1);
              mChart.setSetSpacing(10f);
              mChart.setRoundCorners(10);
              mChart.setAxisBorderValues(0f,24);
              mChart.setLabelsColor(Color.WHITE);
              mChart.setGrid(24,0,axis);
              mChart.setYAxis(false);
              mChart.setTypeface(
                  Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Semibold.ttf"));
            }


          }

          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(getContext(),
                    R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });


}

}
package com.fitkits.Services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.fitkits.Misc.Constants;
import com.fitkits.Home.Activities.HomeActivity;
import com.fitkits.Model.ActivityAnswer;
import com.fitkits.Model.CalorieAnswer;
import com.fitkits.Model.SleepAnswer;
import com.fitkits.Model.WaterAnswer;
import com.fitkits.Model.WeightAnswer;
import com.fitkits.R;
import com.fitkits.RealmObjects.AnswerLog;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.RealmObjects.CurrentWeight;
import com.fitkits.RealmObjects.RecordedDate;
import com.fitkits.RealmObjects.Response;
import com.fitkits.RealmObjects.User_;
import com.fitkits.RealmObjects.Water_Response;
import com.fitkits.RealmObjects.Weight_Response;
import com.fitkits.Misc.RetroClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import okhttp3.ResponseBody;

public class ServerSync extends JobService {
    Realm realmUI;
    AnswerLog answerLog;
    SharedPreferences myPrefs;
    RealmList<Response> goalList;
  RealmList<Water_Response> water_responses=new RealmList<Water_Response>();
  RealmList<Weight_Response> weight_responses=new RealmList<Weight_Response>();
ApiService apiService;
  User_ user_profile;
    RecordedDate recordedDate;
    CurrentWeight currentWeight;

    @Override
    public boolean onStartJob(JobParameters job) {

        Realm.init(getApplicationContext());
        realmUI=Realm.getDefaultInstance();
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       apiService= RetroClient
          .getApiService(myPrefs.getString("token", ""),getApplicationContext());
      SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
      final Calendar myCalendar = Calendar.getInstance();
      String user=myPrefs.getString("user_id","");
      sdf.setTimeZone(TimeZone.getTimeZone("IST"));
      String current=sdf.format(myCalendar.getTime());
      WeightAnswer weightAnswer = realmUI.where(WeightAnswer.class).equalTo("date", current).equalTo("serverLog",0).findFirst();
      if(weightAnswer!=null){
        weightAnswer=new WeightAnswer(user,"weight",weightAnswer.getValue(),weightAnswer.getGoalEndTime(),current,0);
        if(Constants.isNetworkAvailable(getApplicationContext())){
          networkSyncWeight(weightAnswer,current);
        }

      }
      WaterAnswer waterAnswer = realmUI.where(WaterAnswer.class).equalTo("date", current).equalTo("serverLog",0).findFirst();
      if(waterAnswer!=null){
        waterAnswer=new WaterAnswer(user,"waterConsumptionPerDay",waterAnswer.getValue(),current,0);
        if(Constants.isNetworkAvailable(getApplicationContext())){
          networkSyncWater(waterAnswer,current);
        }

      }
      SleepAnswer sleepAnswer = realmUI.where(SleepAnswer.class).equalTo("date", current).equalTo("serverLog",0).findFirst();
      if(sleepAnswer!=null){
        sleepAnswer=new SleepAnswer(user,"sleepDurationPerDay",sleepAnswer.getValue(),current,0);
        if(Constants.isNetworkAvailable(getApplicationContext())){
          networkSyncSleep(sleepAnswer,current);
        }

      }
      CalorieAnswer calorieAnswer = realmUI.where(CalorieAnswer.class).equalTo("date", current).equalTo("serverLog",0).findFirst();
      if(calorieAnswer!=null){
        calorieAnswer=new CalorieAnswer(user,"caloriesPerDay",calorieAnswer.getValue(),current,0);
        if(Constants.isNetworkAvailable(getApplicationContext())){
          networkSyncCalorie(calorieAnswer,current);
        }

      }
      ActivityAnswer activityAnswer = realmUI.where(ActivityAnswer.class).equalTo("date", current).equalTo("serverLog",0).findFirst();
      if(activityAnswer!=null){
        activityAnswer=new ActivityAnswer(user,"activePerDay",activityAnswer.getValue(),current,0);
        if(Constants.isNetworkAvailable(getApplicationContext())){
          networkSyncActive(activityAnswer,current);
        }

      }

      return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }

  void networkSyncWeight(WeightAnswer weightAnswer,String current){

    apiService.logWeight(weightAnswer)
        .subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(ResponseBody value) {


            WeightAnswer weightAnswer1 = realmUI.where(WeightAnswer.class).equalTo("date", current).findFirst();

            realmUI.beginTransaction();
            weightAnswer1.setServerLog(1);
            realmUI.commitTransaction();
            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Daily Weight", getString(R.string.NOTIFICATION_DATA_LOGGED_SUCCESS), 0,"All");



          }

          @Override
          public void onError(Throwable e) {
            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Error Syncing", getString(R.string.NOTIFICATION_DATA_LOGGED_FAILED), 0,"All");

          }

          @Override
          public void onComplete() {

          }
        });
  }

  void networkSyncWater(WaterAnswer waterAnswer,String current){
    ApiService apiService= RetroClient
        .getApiService(myPrefs.getString("token", ""),getApplicationContext());
    apiService.logWater(waterAnswer)
        .subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(ResponseBody value) {


            WaterAnswer waterAnswer1 = realmUI.where(WaterAnswer.class).equalTo("date", current).findFirst();
            realmUI.beginTransaction();
            waterAnswer1.setServerLog(1);
            realmUI.commitTransaction();
            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Daily Water Consumption", getString(R.string.NOTIFICATION_DATA_LOGGED_SUCCESS), 0,"All");



          }

          @Override
          public void onError(Throwable e) {

            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Error Syncing", getString(R.string.NOTIFICATION_DATA_LOGGED_FAILED), 0,"All");

          }

          @Override
          public void onComplete() {

          }
        });
  }
  void networkSyncSleep(SleepAnswer sleepAnswer,String current){
    ApiService apiService= RetroClient
        .getApiService(myPrefs.getString("token", ""),getApplicationContext());
    apiService.logSleep(sleepAnswer)
        .subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(ResponseBody value) {


            SleepAnswer sleepAnswer1 = realmUI.where(SleepAnswer.class).equalTo("date", current).findFirst();
            realmUI.beginTransaction();
            sleepAnswer1.setServerLog(1);
            realmUI.commitTransaction();
            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Daily Sleep Hours", getString(R.string.NOTIFICATION_DATA_LOGGED_SUCCESS), 0,"All");


          }

          @Override
          public void onError(Throwable e) {

            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Error Syncing", getString(R.string.NOTIFICATION_DATA_LOGGED_FAILED), 0,"All");


          }

          @Override
          public void onComplete() {

          }
        });
  }


  void networkSyncCalorie(CalorieAnswer calorieAnswer,String current){
    ApiService apiService= RetroClient
        .getApiService(myPrefs.getString("token", ""),getApplicationContext());
    apiService.logCalorie(calorieAnswer)
        .subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(ResponseBody value) {


            CalorieAnswer calorieAnswer1 = realmUI.where(CalorieAnswer.class).equalTo("date", current).findFirst();

            realmUI.beginTransaction();
            calorieAnswer1.setServerLog(1);
            realmUI.commitTransaction();
            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Daily Calorie intake", getString(R.string.NOTIFICATION_DATA_LOGGED_SUCCESS), 0,"All");



          }

          @Override
          public void onError(Throwable e) {

            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Error Syncing", getString(R.string.NOTIFICATION_DATA_LOGGED_FAILED), 0,"All");

          }

          @Override
          public void onComplete() {

          }
        });
  }


  void networkSyncActive(ActivityAnswer activityAnswer,String current){
    ApiService apiService= RetroClient
        .getApiService(myPrefs.getString("token", ""),getApplicationContext());
    apiService.logActivity(activityAnswer)
        .subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(ResponseBody value) {

            ActivityAnswer activityAnswer1 = realmUI.where(ActivityAnswer.class).equalTo("date", current).findFirst();

            realmUI.beginTransaction();
            activityAnswer1.setServerLog(1);
            realmUI.commitTransaction();

            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Daily Active Hours", getString(R.string.NOTIFICATION_DATA_LOGGED_SUCCESS), 0,"All");



          }

          @Override
          public void onError(Throwable e) {

            NotificationScheduler.showNotification(getApplicationContext(), HomeActivity.class,"Error Syncing", getString(R.string.NOTIFICATION_DATA_LOGGED_FAILED), 0,"All");

          }

          @Override
          public void onComplete() {

          }
        });
  }
}

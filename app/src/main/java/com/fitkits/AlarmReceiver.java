package com.fitkits;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.fitkits.Analytics.Sleep.SleepGraphActivity;
import com.fitkits.Model.*;
import com.fitkits.Model.User;
import io.realm.Realm;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    Realm realmUI;

    @Override
    public void onReceive(Context context, Intent intent) {
        LocalData localData = new LocalData(context);
        Realm.init(context);
        realmUI = Realm.getDefaultInstance();


        //Trigger the notification
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                NotificationScheduler.setReminder(context, AlarmReceiver.class,
                    localData.get_hour(), localData.get_min(),
                    intent.getIntExtra("requestCode", 0));
                return;
            }
        }
        if(localData.get_count()<5) {

            String goal_id="";
            realmUI.beginTransaction();
            final Calendar myCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("IST"));
            Log.d("TIME",sdf.format(myCalendar.getTime()));
            WeightAnswer weightAnswer = realmUI.where(WeightAnswer.class).equalTo("date", sdf.format(myCalendar.getTime())).findFirst();

            if (weightAnswer == null) {

                NotificationScheduler.showNotification(context, HomeActivity.class,"Weight", "Please log your weight.",
                    intent.getIntExtra("requestCode", 0),"weight");
            } else {
                WaterAnswer waterAnswer = realmUI.where(WaterAnswer.class).equalTo("date", sdf.format(myCalendar.getTime())).findFirst();
                if (waterAnswer == null) {

                    NotificationScheduler.showNotification(context, HomeActivity.class,"Water", "Please log your water intake.",
                        intent.getIntExtra("requestCode", 0),"water");
                }
                else {
                    User userMasterGoal = realmUI.where(User.class).findFirst();
                    CalorieAnswer calorieAnswer = realmUI.where(CalorieAnswer.class).equalTo("date", sdf.format(myCalendar.getTime())).findFirst();
                    if ((calorieAnswer == null)||calorieAnswer.getValue()<userMasterGoal.getGoals().getCaloriesPerDay().getValue()) {

                        NotificationScheduler.showNotification(context, HomeActivity.class,"Calorie", "Please log your calorie intake.",
                            intent.getIntExtra("requestCode", 0),"calorie");
                    }
                    else{
                        SleepAnswer sleepAnswer = realmUI.where(SleepAnswer.class).equalTo("date", sdf.format(myCalendar.getTime())).findFirst();
                        if (sleepAnswer == null) {

                            NotificationScheduler.showNotification(context, HomeActivity.class,"Sleep", "Please log your sleep hours.",
                                intent.getIntExtra("requestCode", 0),"sleep");
                        }

                        else{
                            ActivityAnswer activityAnswer = realmUI.where(ActivityAnswer.class).equalTo("date", sdf.format(myCalendar.getTime())).findFirst();
                            if (activityAnswer == null) {

                                NotificationScheduler.showNotification(context, HomeActivity.class,"Active Hours", "Please log your sleep hours.",
                                    intent.getIntExtra("requestCode", 0),"activity");
                            }
                        }
                    }

                }

            }
            int c=localData.get_count();
            localData.set_count(c+1);
            realmUI.commitTransaction();

        }
        else if(localData.get_count()==5) {
            Log.d("JOB","scheduled");
            scheduleJob(context);
            /*NotificationScheduler.setReminder(context, AlarmReceiver.class,
                localData.get_hour(), localData.get_min(),
                intent.getIntExtra("requestCode", 0));*/
            localData.reset_count();
        }

    }
    public void scheduleJob(Context context){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job myJob = dispatcher.newJobBuilder()
            // the JobService that will be called
            .setService(ServerSync.class)
            // uniquely identifies the job
            .setTag("Sync answers")
            // one-off job
            .setRecurring(false)
            // don't persist past a device reboot
            .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
            // start between 0 and 60 seconds from now
            .setTrigger(Trigger.executionWindow(0, 60))
            // don't overwrite an existing job with the same tag
            .setReplaceCurrent(true)
            // retry with exponential backoff
            // constraints that need to be satisfied for the job to run
            .setConstraints(
                // only run on an unmetered network
                Constraint.ON_UNMETERED_NETWORK
            )
            .build();

        dispatcher.mustSchedule(myJob);
    }
}
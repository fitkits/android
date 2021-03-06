package com.fitkits;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.fitkits.Answers.LogActiveDialog;
import com.fitkits.Answers.LogCalorieDialog;
import com.fitkits.Answers.LogSleepDialog;
import com.fitkits.Answers.LogWaterDialog;
import com.fitkits.Answers.LogWeightDialog;
import com.fitkits.CustomCalendarView.CaldroidFragment;
import io.realm.Realm;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements LogWeightDialog.InterfaceCommunicator,LogWaterDialog.InterfaceCommunicator,LogActiveDialog.InterfaceCommunicator,LogCalorieDialog.InterfaceCommunicator,LogSleepDialog.InterfaceCommunicator {
  public  static LinearLayout home,fitkit_news,attendance,profile,home_select,news_select,att_select,profile_select;
  static int WEIGHT_CODE=1,WATER_CODE=2;
  android.support.v4.app.FragmentManager fragmentManager;
LocalData localDataWater,localDataWeight;
public static HomeActivity homeActivity;
TextView toolbar_date;
SharedPreferences myPrefs;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    setSupportActionBar(findViewById(R.id.appbar));
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    Realm.init(getApplicationContext());
    homeActivity=this;
    if(getIntent()!=null){
      if(getIntent().getStringExtra("goal_id")!=null) {
        if(getIntent().getStringExtra("goal_id").equals("weight")) {
          LogWeightDialog logWeightDialog = new LogWeightDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logWeightDialog.setArguments(bundle);
          logWeightDialog.show(getSupportFragmentManager(), "Weight");
        }
        else if(getIntent().getStringExtra("goal_id").equals("water")) {
          LogWaterDialog logWaterDialog = new LogWaterDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logWaterDialog.setArguments(bundle);
          logWaterDialog.show(getSupportFragmentManager(), "water");
        }
        else if(getIntent().getStringExtra("goal_id").equals("calorie")) {
          LogCalorieDialog logCalorieDialog = new LogCalorieDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logCalorieDialog.setArguments(bundle);
          logCalorieDialog.show(getSupportFragmentManager(), "Calorie");
        }
        else if(getIntent().getStringExtra("goal_id").equals("sleep")) {
          LogSleepDialog logSleepDialog = new LogSleepDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logSleepDialog.setArguments(bundle);
          logSleepDialog.show(getSupportFragmentManager(), "Sleep");
        }
        else if(getIntent().getStringExtra("goal_id").equals("activity")) {
          LogActiveDialog logActiveDialog = new LogActiveDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logActiveDialog.setArguments(bundle);
          logActiveDialog.show(getSupportFragmentManager(), "Active Hours");
        }

      }    }


    home=(LinearLayout)findViewById(R.id.home);
    fitkit_news=(LinearLayout)findViewById(R.id.fitkit_news);
    attendance=(LinearLayout)findViewById(R.id.attendance);
    profile=(LinearLayout)findViewById(R.id.profile);
    home_select=(LinearLayout)findViewById(R.id.home_select);
    news_select=(LinearLayout)findViewById(R.id.news_select);
    att_select=(LinearLayout)findViewById(R.id.att_select);
    profile_select=(LinearLayout)findViewById(R.id.profile_select);
    toolbar_date=(TextView) findViewById(R.id.toolbar_date);

    Calendar cal=Calendar.getInstance();
    SimpleDateFormat sdf=new SimpleDateFormat("EEEE  dd.MM.yyyy");
    toolbar_date.setText(sdf.format(cal.getTime()));


    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
    home_select.setVisibility(View.VISIBLE);

    home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        home_select.setVisibility(View.VISIBLE);
        news_select.setVisibility(View.INVISIBLE);
        att_select.setVisibility(View.INVISIBLE);
        profile_select.setVisibility(View.INVISIBLE);
      }
    });
    attendance.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new CaldroidFragment()).commit();

        home_select.setVisibility(View.INVISIBLE);
        news_select.setVisibility(View.INVISIBLE);
        att_select.setVisibility(View.VISIBLE);
        profile_select.setVisibility(View.INVISIBLE);

      }
    });
    fitkit_news.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //startActivity(new Intent(HomeActivity.this,DummyActivity.class));
        home_select.setVisibility(View.INVISIBLE);
        news_select.setVisibility(View.VISIBLE);
        att_select.setVisibility(View.INVISIBLE);
        profile_select.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new FitkitsNewsFragment()).commit();

      }
    });
    profile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();
        home_select.setVisibility(View.INVISIBLE);
        news_select.setVisibility(View.INVISIBLE);
        att_select.setVisibility(View.INVISIBLE);
        profile_select.setVisibility(View.VISIBLE);
      }
    });
    getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
    if(getIntent().getStringExtra("goal_id")==null) {
      localDataWeight=new LocalData(getApplicationContext());
      Calendar rightNow = Calendar.getInstance();
      int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
      int currentMin = rightNow.get(Calendar.MINUTE);

      localDataWeight.set_hour(currentHour);
      localDataWeight.set_min(currentMin+1);

      localDataWeight.reset_count();
      NotificationScheduler
          .setReminder(HomeActivity.this, AlarmReceiver.class, localDataWeight.get_hour(),
              localDataWeight.get_min(), WEIGHT_CODE);
    }

  }


  @Override
  public void refresh() {

  }

  @Override
  public void refresh(int pos) {
    if(pos==1) {
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          LogWaterDialog logWaterDialog = new LogWaterDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logWaterDialog.setArguments(bundle);
          logWaterDialog.show(getSupportFragmentManager(), "water");
        }
      }, 300);
    }
    else if(pos==2) {
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          LogActiveDialog logActiveDialog = new LogActiveDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logActiveDialog.setArguments(bundle);
          logActiveDialog.show(getSupportFragmentManager(), "Active Hours");
        }
      }, 300);
    }

    else if(pos==3) {
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          LogSleepDialog logSleepDialog = new LogSleepDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logSleepDialog.setArguments(bundle);
          logSleepDialog.show(getSupportFragmentManager(), "Sleep");
        }
      }, 300);
    }
    else if(pos==4) {
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          LogCalorieDialog logCalorieDialog = new LogCalorieDialog();
          Bundle bundle = new Bundle();
          bundle.putString("type", "home");
          logCalorieDialog.setArguments(bundle);
          logCalorieDialog.show(getSupportFragmentManager(), "Calorie");
        }
      }, 300);
    }

  }
}

package com.fitkits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Model.*;
import com.fitkits.Model.User;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.List;

public class GoalActivity extends AppCompatActivity implements CalorieDialog.InterfaceCommunicator,ActiveDialog.InterfaceCommunicator,WeightDialog.InterfaceCommunicator,SleepDialog.InterfaceCommunicator,WaterDialog.InterfaceCommunicator{

  //LinearLayout calorieLay,activeLay,weightLay,sleepLay,waterLay;
  //CardView calorieCard,activeCard,weightCard,sleepCard,waterCard;
  //TextView calorieText,activeText,weightText,sleepText,waterText,calsettext,calsetdesc,activesettext,activesetdesc,weightsettext,weightsetdesc,sleepsettext,sleepsetdesc,watersettext,watersetdesc;
  String weightDate="",bedtime="",bedtimeampm="",wakeuptime="",wakeuptimeampm="";
  int noofmealspos=0,activehourspos=0,sleepgoalpos=0,waterpos=0;
  Button done;
  RecyclerView goalRecylceView;
  User userMasterGoal;
  SharedPreferences myPrefs;
  private Realm realmUI;

  public static GoalActivity goalActivity;
  @Override
  public void onBackPressed() {

   finish();

  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_goals);
    goalActivity = this;
    myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    done = (Button) findViewById(R.id.done);
    goalRecylceView = (RecyclerView) findViewById(R.id.goalRecylerView);
    goalRecylceView.setLayoutManager(new LinearLayoutManager(this));
    LinearLayout back=(LinearLayout)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
       finish();
      }
    });
    realmUI = Realm.getDefaultInstance();

    realmUI.beginTransaction();
    userMasterGoal=realmUI.where(User.class).findFirst();
    realmUI.commitTransaction();
    if(userMasterGoal!=null) {
      if((userMasterGoal.getGoals().getCaloriesPerDay().getValue()!=0)&&(userMasterGoal.getGoals().getSleepDurationPerDay().getValue()!=0)&&(userMasterGoal.getGoals().getActivePerDay().getValue()!=0)&&(userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()!=0)&&(userMasterGoal.getGoals().getWeight().getValue()!=0)){
        done.setBackgroundColor(getResources().getColor(R.color.black));
        done.setEnabled(true);
      }
      else{
        done.setBackgroundColor(getResources().getColor(R.color.black30));
        done.setEnabled(false);
      }
      goalRecylceView.setAdapter(
          new GoalAdapter(getSupportFragmentManager(), GoalActivity.this, userMasterGoal));
    }

    /*calorieCard=(CardView)findViewById(R.id.calorieCard);
    activeCard=(CardView)findViewById(R.id.activeCard);
    weightCard=(CardView)findViewById(R.id.weightCard);
    sleepCard=(CardView)findViewById(R.id.sleepCard);
    waterCard=(CardView)findViewById(R.id.waterCard);
    calorieLay=(LinearLayout) findViewById(R.id.calLay);
    activeLay=(LinearLayout)findViewById(R.id.activeLay);
    weightLay=(LinearLayout)findViewById(R.id.weightLay);
    sleepLay=(LinearLayout)findViewById(R.id.sleepLay);
    waterLay=(LinearLayout)findViewById(R.id.waterLay);
    calorieText=(TextView) findViewById(R.id.calText);
    activeText=(TextView)findViewById(R.id.activeText);
    weightText=(TextView)findViewById(R.id.weightText);
    sleepText=(TextView)findViewById(R.id.sleepText);
    waterText=(TextView) findViewById(R.id.waterText);
    calsettext=(TextView) findViewById(R.id.calsetText);
    activesettext=(TextView)findViewById(R.id.activesettext);
    weightsettext=(TextView)findViewById(R.id.weightsetText);
    sleepsettext=(TextView)findViewById(R.id.sleepsetText);
    watersettext=(TextView) findViewById(R.id.watersetText);

    calsetdesc=(TextView) findViewById(R.id.calsetDesc);
    activesetdesc=(TextView)findViewById(R.id.activesetdesc);
    weightsetdesc=(TextView)findViewById(R.id.weightsetDesc);
    sleepsetdesc=(TextView)findViewById(R.id.sleepsetDesc);
    watersetdesc=(TextView) findViewById(R.id.watersetDesc);
    calorieText.setText("");
    activeText.setText("");
    weightText.setText("");
    sleepText.setText("");
    waterText.setText("");
    */
    getGoals();




   /* apiService.getUserGoals().subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<User_Profile>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(User_Profile value) {
            AnswerLog.d("MasterGoal",value.toString());


          }

          @Override
          public void onError(Throwable e) {
            AnswerLog.d("MasterGoal",e.getMessage());

            Toast.makeText(GoalActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
          }

          @Override
          public void onComplete() {
            Toast.makeText(GoalActivity.this,"Complete",Toast.LENGTH_LONG).show();

          }
        });

*/
    done.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(GoalActivity.this, HomeActivity.class));
        finish();
      }
    });

  }
    public void getGoals(){
      final ApiService apiService = RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());
      apiService.getUserProfile("/api/v1/cms/users/"+myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<User>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(User value) {

            realmUI.beginTransaction();
            realmUI.copyToRealmOrUpdate(value);
            realmUI.commitTransaction();
            userMasterGoal=value;
            goalRecylceView.setAdapter(new GoalAdapter(getSupportFragmentManager(),GoalActivity.this,userMasterGoal));
            if((userMasterGoal.getGoals().getCaloriesPerDay().getValue()!=0)&&(userMasterGoal.getGoals().getSleepDurationPerDay().getValue()!=0)&&(userMasterGoal.getGoals().getActivePerDay().getValue()!=0)&&(userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()!=0)&&(userMasterGoal.getGoals().getWeight().getValue()!=0)){
              done.setBackgroundColor(getResources().getColor(R.color.black));
              done.setEnabled(true);
            }
            else{
              done.setBackgroundColor(getResources().getColor(R.color.black30));
              done.setEnabled(false);
            }

          }

          @Override
          public void onError(Throwable e) {
            Log.d("response",e.toString());
            Toast.makeText(getApplicationContext(),"Something went wrong. Please try again later.",Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onComplete() {


          }
        });
  }

   /* calorieCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        CalorieDialog calorieDialog=new CalorieDialog();
        Bundle bundle=new Bundle();
        bundle.putString("calories",calorieText.getText().toString());
        bundle.putInt("noofmealspos",noofmealspos);
        calorieDialog.setArguments(bundle);
        calorieDialog.show(getSupportFragmentManager(),"Calorie");
      }
    });

    activeCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        ActiveDialog activeDialog=new ActiveDialog();
        Bundle bundle=new Bundle();
        bundle.putInt("activehourspos",activehourspos);
        activeDialog.setArguments(bundle);
        activeDialog.show(getSupportFragmentManager(),"active");      }
    });
    weightCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        WeightDialog weightDialog=new WeightDialog();
        Bundle bundle=new Bundle();
        if(!weightDate.equals("")) {
          bundle.putString("currentweight", weightText.getText().toString());
          bundle.putString("recordedOn", weightDate);
          weightDialog.setArguments(bundle);
        }

        weightDialog.show(getSupportFragmentManager(),"Weight");
      }
    });
    sleepCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        SleepDialog sleepDialog=new SleepDialog();
        Bundle bundle=new Bundle();

        bundle.putString("bedtime",bedtime);
        bundle.putString("bedtimeampm",bedtimeampm);
        bundle.putString("wakeuptime",wakeuptime);
        bundle.putString("wakeuptimeampm",wakeuptimeampm);
        bundle.putInt("sleepgoalpos",sleepgoalpos);
        sleepDialog.setArguments(bundle);
        sleepDialog.show(getSupportFragmentManager(),"Sleep");
      }
    });
    waterCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        WaterDialog waterDialog=new WaterDialog();
        Bundle bundle=new Bundle();
        bundle.putInt("waterpos",waterpos);
        waterDialog.setArguments(bundle);
        waterDialog.show(getSupportFragmentManager(),"Water");
      }
    });*/













  @Override
  public void refresh() {
   getGoals();
  }

/*
  private List<UserMasterGoal> updateMasterGoal(List<UserMasterGoal> userMasterGoal,User_Profile userProfile) {

    List<Goal> userGoalList=userProfile.getGoals();

    for(int i=0;i<userGoalList.size();i++){
      for(int j=0;j<userMasterGoal.size();j++){

        if(userGoalList.get(i).getGoal().getId().equals(userMasterGoal.get(j).getId())){
          List<DataField> userGoalDataField=userMasterGoal.get(j).getDataFields();
          for(int k=0;k<userGoalDataField.size();k++){
            Log.d("status","works"+userGoalDataField.get(k).getField()+userGoalDataField.size());


            if(userGoalDataField.get(k).getField().equals("currentWeight")){
              userGoalDataField.get(k).setValue(userGoalList.get(i).getData().getCurrentWeight().getValue().toString());
            }
            else if(userGoalDataField.get(k).getField().equals("recordedDate")){
              userGoalDataField.get(k).setValue(userGoalList.get(i).getData().getRecordedon().getValue().toString());
            }
            else if(userGoalDataField.get(k).getField().equals("goalWeight")){
              userGoalDataField.get(k).setValue(userGoalList.get(i).getData().getGoalWeight().getValue().toString());
            }
            else if(userGoalDataField.get(k).getField().equals("goalDate")){
              userGoalDataField.get(k).setValue(userGoalList.get(i).getData().getGoalDate().getValue().toString());
            }
            else if(userGoalDataField.get(k).getField().equals("numberOfCups")){
              userGoalDataField.get(k).setValue(userGoalList.get(i).getData().getNumberOfCups().getValue().toString());
            }





          }
        }
      }
    }


    return userMasterGoal;
  }
  */
}

package com.fitkits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.fitkits.CustomCalendarView.CaldroidFragment;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.Membership;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

  CaldroidFragment caldroidFragment;
  SharedPreferences myPrefs;
  ProgressDialog progressDialog;
  public static LoginActivity loginActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginActivity=this;
   getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, new SplashScreenFragment()).commit();
    /*startActivity(new Intent(LoginActivity.this,GoalActivity.class));
    finish();*/
    caldroidFragment = new CaldroidFragment();
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    Realm.init(getApplicationContext());

    // //////////////////////////////////////////////////////////////////////
    // **** This is to show customized fragment. If you want customized
    // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

    // Setup arguments

    // If Activity is created after rotation
    if (savedInstanceState != null) {
      caldroidFragment.restoreStatesFromKey(savedInstanceState,
          "CALDROID_SAVED_STATE");
    }
    // If activity is created from fresh
    else {
      Bundle args = new Bundle();
      Calendar cal = Calendar.getInstance();
      args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
      args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
      args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
      args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

      // Uncomment this to customize startDayOfWeek
      // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
      // CaldroidFragment.TUESDAY); // Tuesday

      // Uncomment this line to use Caldroid in compact mode
      // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

      // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

      caldroidFragment.setArguments(args);
    }
    new Handler().postDelayed(new Runnable() {
      public void run() {
        if(myPrefs.getString("user_id","").equals("")) {
          getSupportFragmentManager().beginTransaction()
              .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
              .replace(R.id.container, new LoginFragment()).commit();
        }
        else{

          getRenewDetail();
        }

      }
    }, 5000);


  }


  void getRenewDetail() {

    ApiService apiService = RetroClient
        .getApiService(myPrefs.getString("mobileNumber", ""), myPrefs.getString("otp", ""),
           getApplicationContext());

    apiService.getSubscriptions("/api/v1/cms/subscriptions?user="+myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ItemParent>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(ItemParent itemParent) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            List<Membership> value=itemParent.getSubscriptions();
            Log.d("Response", value.toString());
            try {
              long daysRemain = 0;
              if (value.size() != 0) {
                Membership membership = value.get(value.size() - 1);
                String dt = membership.getEndDate();
                SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                Calendar ex = Calendar.getInstance();
                try {
                  ex.setTime(sdf.parse(dt));
                } catch (ParseException e) {
                  e.printStackTrace();
                }
                String currentDate = sdf.format(new Date());
                Calendar cu = Calendar.getInstance();
                double diff = ex.getTimeInMillis() - cu.getTimeInMillis();
                double days = diff / (24 * 60 * 60 * 1000);
                daysRemain = Math.round(days);


              }
              if (value.size() != 0) {
                if(daysRemain>0){
                RenewDialog renewDialog = new RenewDialog();
                  Bundle bundle = new Bundle();
                  bundle.putLong("daysRemain", daysRemain);
                  bundle.putString("start","1");
                  renewDialog.setArguments(bundle);
                renewDialog.show(getSupportFragmentManager(), "");}
                else {

                  Intent intent=new Intent(LoginActivity.this, SubscriptionActivity.class);
                  intent.putExtra("start","1");
                  startActivity(intent);
                }
              } else {
                Intent intent=new Intent(LoginActivity.this, SubscriptionActivity.class);
                intent.putExtra("start","1");
                startActivity(intent);

              }

            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(LoginActivity.this, "Something went wrong. Please try again later.",
                Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });

  }





}


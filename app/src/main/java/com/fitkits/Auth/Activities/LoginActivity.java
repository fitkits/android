package com.fitkits.Auth.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fitkits.Auth.Dialogs.OtpDialog;
import com.fitkits.Model.Migration;
import com.fitkits.OnBoarding.Activities.OnboardActivity;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.CustomCalendarView.CaldroidFragment;
import com.fitkits.Home.Activities.HomeActivity;
import com.fitkits.Auth.Fragments.LoginFragment;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.Membership;
import com.fitkits.Model.User;
import com.fitkits.Misc.Fragments.Pending;
import com.fitkits.R;
import com.fitkits.Dialogs.RenewDialog;
import com.fitkits.Misc.RetroClient;
import com.fitkits.Misc.Fragments.SplashScreenFragment;
import com.fitkits.Subscriptions.SubscriptionActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.exceptions.RealmMigrationNeededException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * =================
 * |Raghu's Comment
 * =================
 * This code has gotten too complex to make it modular
 * without rewrite. So I've made some changes as much
 * as possible and giving you a flow to understand
 * what happens. In the code.
 *
 * NOW GO TO ONCREATE().
 */
public class LoginActivity extends AppCompatActivity {

    CaldroidFragment caldroidFragment;
    SharedPreferences myPrefs;
    ProgressDialog progressDialog;
    boolean pending = true;
    public static LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;
        /**
         * Once the app is launched, the SplashScreenFragment which is just a UI thing with no logic,
         * is launched and after 5000ms, a handler is fired to start the flow. See the Handler below.
         */
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SplashScreenFragment()).commit();
    /*startActivity(new Intent(LoginActivity.this,GoalActivity.class));
    finish();*/
        caldroidFragment = new CaldroidFragment();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Realm.init(getApplicationContext());

            // Get a Realm instance for this thread
            RealmConfiguration config = new RealmConfiguration.Builder()
//                    .deleteRealmIfMigrationNeeded()
                    .schemaVersion(1)
                    .migration(new Migration())
                    .build();

            Realm.setDefaultConfiguration(config);

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
            caldroidFragment.setArguments(args);
        }
        /**
         * This handler is launched after 5000ms and it checks for user_id
         * in the prefs. If it doesnt exist, then we try to launch the
         * LoginFragment, else, we check for PendingState. PendingState is
         * simply a check to see if there is any subscription pending.
         */
        new Handler().postDelayed(new Runnable() {

            public void run() {
                /**
                 * if this if gets to true, the it means we have a
                 * new user and hence, we take him to the LoginFragment.
                 * GO TO LOGIN FRAGMENT FOR THIS FLOW. IF YOU WANT TO CHECK
                 * FLOW OF EXISTING USER, GO TO THE ELSE PART.
                 */
                if (myPrefs.getString("user_id", "").equals("")) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.container, new LoginFragment()).commit();
                } else {
                    /**
                     * this is where the pending subscription is checked.
                     * NOW GO TO THAT FUNCTION DEFINITION.
                     */
                    getPendingstate();

                }

            }
        }, 5000);
    }


    void getRenewDetail() {

//        Toast.makeText(getApplicationContext(),"INSIDE LOGINACTIVITY",Toast.LENGTH_LONG).show();
        ApiService apiService = RetroClient
                .getApiService(myPrefs.getString("token", ""),
                        getApplicationContext());

        apiService.getSubscriptions("/api/v1/cms/subscriptions?user=" + myPrefs.getString("user_id", "")).subscribeOn(
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
                        List<Membership> value = itemParent.getSubscriptions();
                        Log.d("Response", value.toString());
                        try {
                            long daysRemain = 0;
                            if (value.size() != 0) {
                                Membership membership = value.get(value.size() - 1);
                                String dt = membership.getEndDate();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
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

                                if (daysRemain < 8 && daysRemain > 0) {
                                    RenewDialog renewDialog = new RenewDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putLong("daysRemain", daysRemain);
                                    bundle.putString("start", "1");
                                    renewDialog.setArguments(bundle);
                                    renewDialog.show(getSupportFragmentManager(), "");
                                } else {
                                    /**
                                     * for bypassing to test. comment on prod
                                     */
//                                    if(false) {
                                      if (daysRemain > 8) {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        // intent.putExtra("start", "1");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                                        intent.putExtra("start", "1");
                                        startActivity(intent);


                                    }


                                }
                            } else {
                                Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                                intent.putExtra("start", "1");
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
                        Toast.makeText(LoginActivity.this, R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                                Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getPendingstate() {
        /**
         * This is the code that checks for the pending subscription.
         * Previously, It didnt had checks for the scenario where
         * the user clicks home button in phone, or exits the app without
         * filling the onboarding form. Ive made sure that that scenario
         * is covered.
         */
        ApiService apiService = RetroClient
                .getApiService(myPrefs.getString("token", ""),
                        getApplicationContext());
        apiService.getPendingMembership("/api/v1/cms/users/" + myPrefs.getString("user_id", "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading....");
                progressDialog.show();
                progressDialog.setCancelable(false);

            }

            @Override
            public void onNext(User value) {
                 Log.d("RAGHU", value.getHeight() + "/" + value.getWeight() + "/" + value.getGender());

                /**
                 * This small check verifies if every piece of onBoarding information
                 * is taken from user before showing him the app.
                 * There are some data like DOB and Gender which are not
                 * Checked because they are the middle screens in the onboarding
                 * screens. Their presence is already checked by making sure
                 * their next screen value is captured.
                 */

                boolean isHeightNull = (value.getHeight() == null);
                boolean isWeightNull = (value.getWeight() == null);
                boolean isFoodPreferenceNull  = (value.getFoodPreference() == null);
                boolean isBloodGroupNull  = (value.getBloodGroup() == null);
                if(isHeightNull || isWeightNull || isFoodPreferenceNull || isBloodGroupNull) {
                    /**
                     * If that condition fails, Then OnBoarding is started again. This flow happens
                     * again and again until the user gives us all data we need.
                     */

                    startActivity(new Intent(LoginActivity.this, OnboardActivity.class));
                    finish();
                }
                else {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    /**
                     * If we've got all the data we need, It means, we have successfully onboarded
                     * the user who had missing details.
                     *
                     * NOTE: New users dont come under this flow because they will have their
                     * "user_id" set to "" and hence the initial check we did in OnCreate will
                     * take them to the login fragment right away. This is only for users, who have
                     * missed their onBoarding or had data inconsistencies.
                     *
                     * NOTE: existing users who had missed their onBoarding details
                     * will have their details updated with this new release.
                     */
                    pending = value.getPendingMembership().getIsPending();
                    if (pending) {
                        /**
                         * If they had successfully registered and Onboarded,
                         * then they will be shown the Pending subscription screen
                         * until they subscribe.
                         */
                        Pending pending = new Pending();
                        pending.show(getSupportFragmentManager(), "");
                    }
                    //if (pending)
                    if (!pending) {
                        /**
                         * at this point, the user is a valid consistent user, with an active
                         * subscription. So we take them to the HomeActivity.
                         */

                        getRenewDetail();
//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        // intent.putExtra("start", "1");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                /**
                 * This is to Handle the case of inconsistent
                 * users. Who have thier perfs filled but their
                 * Auth failed due to changes in DB or deleted user.
                 * We just clear their data and ask them to login again.
                 */
                if(e.getMessage().contains("HTTP 401 Unauthorized")) {
                    // we do this to trigger OnBoard in cases where the app uses
                    // orphaned perfs.
                    myPrefs.edit().putString("user_id","").commit();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.container, new LoginFragment()).commit();
                }
                Toast.makeText(getApplicationContext(), "Session Expired! Please Login Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onComplete() {

            }
        });
    }


}




package com.fitkits.Answers;

import static com.fitkits.Analytics.Sleep.SleepGraphActivity.sleepGraphActivity;
import static com.fitkits.Home.Activities.HomeActivity.homeActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.Constants;
import com.fitkits.Model.SleepAnswer;
import com.fitkits.Model.User;
import com.fitkits.R;
import com.fitkits.Misc.RetroClient;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import okhttp3.ResponseBody;

/**
 * Created by akshay on 10/07/17.
 */
public class LogSleepDialog extends BlurDialogFragment {

    Button done;
    SharedPreferences myPrefs;

    ProgressDialog progressDialog;
CardView weightDateLay;
ApiService apiService;
SleepAnswer sleepAnswer;
Realm realmUI;
String current;
CheckBox terrible,bad,decent,good,great;
TextView ratingText;
int rating=0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.log_sleep);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM   );
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        Realm.init(getActivity());
        realmUI = Realm.getDefaultInstance();
        done=(Button) dialog.findViewById(R.id.done);
        terrible=(CheckBox) dialog.findViewById(R.id.terrible);
        bad=(CheckBox) dialog.findViewById(R.id.bad);
        decent=(CheckBox) dialog.findViewById(R.id.decent);
        good=(CheckBox) dialog.findViewById(R.id.good);
        great=(CheckBox) dialog.findViewById(R.id.great);
        ratingText=(TextView) dialog.findViewById(R.id.ratingText);




        if(great.isChecked()==true){
            ratingText.setText("Great.");
        }
        else if(good.isChecked()==true){
            ratingText.setText("Good.");
        }
        else if(decent.isChecked()==true){
            ratingText.setText("Decent.");
        }
        else if(bad.isChecked()){
            ratingText.setText("Bad.");
        }
        else if(terrible.isChecked()){
            ratingText.setText("Terrible.");
        }


        terrible.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    ratingText.setText("Terrible.");
                    rating=1;
                }
                else{
                    bad.setChecked(false);
                    decent.setChecked(false);
                    good.setChecked(false);
                    great.setChecked(false);
                    ratingText.setText("");
                    rating=0;
                }

            }
        });


        bad.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    terrible.setChecked(true);
                    ratingText.setText("Bad.");
                    rating=2;
                }
                else{
                    decent.setChecked(false);
                    good.setChecked(false);
                    great.setChecked(false);
                    rating=1;
                    ratingText.setText("Terrible.");
                }

            }
        });

        decent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    terrible.setChecked(true);
                    bad.setChecked(true);
                    ratingText.setText("Decent.");
                    rating=3;
                }
                else{
                    good.setChecked(false);
                    great.setChecked(false);
                    rating=2;
                    ratingText.setText("Bad.");
                }

            }
        });
        good.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    terrible.setChecked(true);
                    bad.setChecked(true);
                    decent.setChecked(true);
                    ratingText.setText("Good.");
                    rating=4;
                }
                else{
                    great.setChecked(false);
                    rating=3;
                    ratingText.setText("Decent.");
                }

            }
        });
        great.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    terrible.setChecked(true);
                    bad.setChecked(true);
                    decent.setChecked(true);
                    good.setChecked(true);
                    ratingText.setText("Great.");
                    rating=5;
                }
                else{
                    rating=4;
                    ratingText.setText("Good.");
                }

            }
        });



        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                        if(rating>0){
                            logSleep(rating,"sleepDurationPerDay");

                        }
                        else{
                            Toast.makeText(getActivity(),"Please rate out of 5.",Toast.LENGTH_SHORT).show();

                        }


            }
        });





        return dialog;
    }

    public interface InterfaceCommunicator {
        void  refresh();
        void  refresh(int pos);


    }

    @Override
    public void onStart() {
        super.onStart();
        //       if(googleApiClient!=null)
    }

    @NonNull
    @Override
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder()
                .overlayColor(Color.argb(136, 0, 0, 0))  // semi-transparent white color
                .debug(true)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


   void logSleep(int rating,String type)  {
        try {

            String user = myPrefs.getString("user_id", "");
            SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
            sdf_new.setTimeZone(TimeZone.getTimeZone("IST"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            Calendar cal = Calendar.getInstance();
            current = sdf_new.format(cal.getTime());
            User userMasterGoal=realmUI.where(User.class).findFirst();
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            float indSleep=userMasterGoal.getGoals().getSleepDurationPerDay().getValue()/5;
            float avgrating=indSleep*rating;
            float avgSleep=Float.parseFloat(decimalFormat.format(avgrating));

                sleepAnswer = new SleepAnswer(user, type,avgSleep, current, 0);

                realmUI.beginTransaction();
                realmUI.copyToRealmOrUpdate(sleepAnswer);
                realmUI.commitTransaction();


                if(Constants.isNetworkAvailable(getContext())){
                    networkSync(sleepAnswer);

                }
                else{
                    Constants.internetToastButLogged(getContext());
                    dismiss();

                }



        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    void networkSync(SleepAnswer sleepAnswer){
        ApiService apiService= RetroClient
            .getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());
        apiService.logSleep(sleepAnswer)
            .subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable d) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                }

                @Override
                public void onNext(ResponseBody value) {

                    if (progressDialog.isShowing() && this != null) {
                        progressDialog.dismiss();
                    }

                    SleepAnswer sleepAnswer1 = realmUI.where(SleepAnswer.class).equalTo("date", current).findFirst();
                    realmUI.beginTransaction();
                    sleepAnswer1.setServerLog(1);
                    realmUI.commitTransaction();
                    if (getArguments() != null) {
                        if(getArguments().getString("type").equals("home")) {
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) homeActivity;
                            interfaceCommunicator.refresh(4);
                            dismiss();
                        }
                        else{
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) sleepGraphActivity;
                            interfaceCommunicator.refresh();
                            dismiss();
                        }
                    }



                }

                @Override
                public void onError(Throwable e) {
                    if (progressDialog.isShowing() && this != null) {
                        progressDialog.dismiss();
                    }
                    Log.d("Response", e.getMessage());
                    Toast.makeText(getActivity(),
                        "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onComplete() {

                }
            });
    }


}



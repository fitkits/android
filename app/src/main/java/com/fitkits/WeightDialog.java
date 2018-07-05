package com.fitkits;

import static com.fitkits.Analytics.Weight.WeightGraphActivity.weightGraphActivity;
import static com.fitkits.GoalActivity.goalActivity;
import static com.fitkits.Analytics.Water.WaterGraphActivity.waterGraphActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Model.*;
import com.fitkits.Model.User;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by akshay on 10/07/17.
 */
public class WeightDialog extends BlurDialogFragment {

    Button done;
    User user_profile;
    SharedPreferences myPrefs;
    TextView currentWeight,currentWeightDate;
    EditText goalWeight;
    TextView goalWeightDate;
    LinearLayout currentweightLay;
    ProgressDialog progressDialog;
CardView weightDateLay;
Realm realmUI;
ApiService apiService;
    com.fitkits.Model.User userMasterGoal;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.set_weight);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM   );
        realmUI=Realm.getDefaultInstance();
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        weightDateLay=(CardView)dialog.findViewById(R.id.weightDateLay);
        currentweightLay=(LinearLayout)dialog.findViewById(R.id.currentweightLay);
        currentWeight=(TextView) dialog.findViewById(R.id.currentWeight);
        goalWeightDate=(TextView) dialog.findViewById(R.id.weightDate);
        currentWeightDate=(TextView)dialog.findViewById(R.id.currentWeightDate);
        goalWeight=(EditText)dialog.findViewById(R.id.weight);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
        realmUI.beginTransaction();
        userMasterGoal = realmUI.where(User.class).findFirst();

        realmUI.commitTransaction();
        if(userMasterGoal!=null) {
            goalWeight.setText(userMasterGoal.getGoals().getWeight().getValue().toString());
            goalWeight.setSelection(goalWeight.getText().length());
            try {
                goalWeightDate.setText(sdf_new.format(sdf.parse(userMasterGoal.getGoals().getWeight().getWeightGoalDate().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

           /*
            try {
                goalWeightDate
                    .setText(sdf_new.format(sdf.parse(userMasterGoal.getDataFields().get(3).getValue())));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
        done=(Button) dialog.findViewById(R.id.next);


        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goalWeight.getText().toString().length()>0) {
                    if(goalWeightDate.getText().toString().length()>0) {

                                setWeightGoal(Integer.parseInt(goalWeight.getText().toString()),goalWeightDate.getText().toString());

                        }

                    else{
                        Toast.makeText(getActivity(),"Please select the date.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Please enter the weight.",Toast.LENGTH_SHORT).show();
                }

            }
        });
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener validity= new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                sdf.setTimeZone(TimeZone.getTimeZone("IST"));
                goalWeightDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        goalWeightDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                weightDateLay.callOnClick();
            }
        });

        weightDateLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme,validity, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        try {

            currentWeight.setText(myPrefs.getString("recorded_weight", ""));

            if(!myPrefs.getString("recorded_date", "").equals(""))
            currentWeightDate
                .setText(sdf_new.format(sdf.parse(myPrefs.getString("recorded_date", ""))));
        }
        catch (Exception e){

        }




        return dialog;
    }

    public interface InterfaceCommunicator {
        void  refresh();
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

    void setWeightGoal(int weight,String weightGoalDate)  {
        apiService=RetroClient.getApiService(myPrefs.getString("mobileNumber",""),myPrefs.getString("otp",""),getActivity().getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
        String formattedGoalDate="";
        try {
             formattedGoalDate=sdf.format(sdf_new.parse(weightGoalDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Weight setWeight=new Weight(weight,formattedGoalDate);
            Goals goal=new Goals(setWeight);
        user_profile = new User(goal);

        apiService.updateProfile("/api/v1/cms/users/" + myPrefs.getString("user_id", ""), user_profile)
            .subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Observer<User>() {
                @Override
                public void onSubscribe(Disposable d) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                }

                @Override
                public void onNext(User value) {

                    if (progressDialog.isShowing() && this != null) {
                        progressDialog.dismiss();
                    }
                    /*SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd'T'hh:mm:ss.SSS'Z'");
                    SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");*/
                    realmUI.beginTransaction();
                    userMasterGoal.getGoals().getWeight().setValue(Integer.parseInt(goalWeight.getText().toString()));
                    /*try {
                        userMasterGoal.getDataFields().get(3).setValue(sdf.format(sdf_new.parse(goalWeightDate.getText().toString())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                    realmUI.commitTransaction();
                    if(getArguments()!=null) {
                        if(getArguments().getString("type").equals("goal")) {
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) goalActivity;
                            interfaceCommunicator.refresh();
                            dismiss();
                        }
                        else{
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) weightGraphActivity;
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



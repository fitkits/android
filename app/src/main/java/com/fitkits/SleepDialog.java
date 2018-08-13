package com.fitkits;

import static com.fitkits.Analytics.Sleep.SleepGraphActivity.sleepGraphActivity;
import static com.fitkits.GoalActivity.goalActivity;
import static com.fitkits.Analytics.Water.WaterGraphActivity.waterGraphActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by akshay on 10/07/17.
 */
public class SleepDialog extends BlurDialogFragment {

    CardView bedtimeCard,wakeuptimeCard,sleepCard;
    Spinner sleepgoal;
    TextView bedtime,bedtimeampm,wakeuptime,wakeuptimeampm;
    Button done;
    List<String> sleeplist=new ArrayList<String>();
    Realm realmUI;
    com.fitkits.Model.User userMasterGoal;
    SharedPreferences myPrefs;
    Date wakeup,bed;
    ApiService apiService;
    ProgressDialog progressDialog;
    int bselH=00,bselM=00,wselH=00,wselM=00;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.set_sleep);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        realmUI=Realm.getDefaultInstance();

        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        bedtimeCard=(CardView) dialog.findViewById(R.id.bedtimeCard);
        wakeuptimeCard=(CardView) dialog.findViewById(R.id.wakeuptimeCard);
        sleepCard=(CardView) dialog.findViewById(R.id.sleepCard);

        sleepgoal=(Spinner) dialog.findViewById(R.id.sleepgoal);
        bedtime=(TextView) dialog.findViewById(R.id.bedtime);
        bedtimeampm=(TextView) dialog.findViewById(R.id.bedtimeampm);
        wakeuptime=(TextView) dialog.findViewById(R.id.wakeuptime);
        wakeuptimeampm=(TextView) dialog.findViewById(R.id.wakeupampm);

        done=(Button) dialog.findViewById(R.id.next);
        sleeplist.add("XX");
        for(int i=1;i<25;i++){
            if(i<10)
                sleeplist.add("0"+String.valueOf(i));
            else
                sleeplist.add(String.valueOf(i));

        }
        sleepgoal.setAdapter(new SpinnerAdapter(getContext(),R.layout.year_item,sleeplist));

        realmUI.beginTransaction();
        userMasterGoal = realmUI.where(com.fitkits.Model.User.class).findFirst();
        realmUI.commitTransaction();
        if(userMasterGoal!=null) {
            String ah="";
            int sleepHour=userMasterGoal.getGoals().getSleepDurationPerDay().getValue();
            if(sleepHour<10){
                ah="0"+String.valueOf(sleepHour);
            }
            else{
                ah=String.valueOf(sleepHour);
            }
            for(int i=1;i<25;i++){
                if(ah.equals(sleeplist.get(i))){
                    sleepgoal.setSelection(i);
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat sdf_H = new SimpleDateFormat("HH");
            SimpleDateFormat sdf_M = new SimpleDateFormat("mm");

            SimpleDateFormat sdf_new = new SimpleDateFormat("h:mm");
            SimpleDateFormat sdf_new_unit = new SimpleDateFormat("a");
            try {
                wakeup=sdf.parse(userMasterGoal.getGoals().getSleepDurationPerDay().getWakeUpTime());
                wakeuptime.setText(sdf_new.format(wakeup));
                wakeuptimeampm.setText(sdf_new_unit.format(wakeup));
                wselH=Integer.valueOf(sdf_H.format(wakeup));
                wselM=Integer.valueOf(sdf_M.format(wakeup));

                bed=sdf.parse(userMasterGoal.getGoals().getSleepDurationPerDay().getBedTime());
                bedtime.setText(sdf_new.format(bed));
                bedtimeampm.setText(sdf_new_unit.format(bed));
                bselH=Integer.valueOf(sdf_H.format(bed));
                bselM=Integer.valueOf(sdf_M.format(bed));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        final Calendar myCalendarSD = Calendar.getInstance();
        sleepCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sleepgoal.performClick();
            }
        });
        bedtimeCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                mcurrentTime.setTime(bed);
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),R.style.DatePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        bselH=selectedHour;
                        bselM=selectedMinute;
                        bedtime.setText(getTime(selectedHour,selectedMinute));
                        bedtimeampm.setText(getTimeAmPm(selectedHour,selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        wakeuptimeCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                mcurrentTime.setTime(wakeup);
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),R.style.DatePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        wselH=selectedHour;
                        wselM=selectedMinute;
                        wakeuptime.setText(getTime(selectedHour,selectedMinute));
                        wakeuptimeampm.setText(getTimeAmPm(selectedHour,selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bedtime.getText().toString().length() > 0) {
                    if (wakeuptime.getText().toString().length() > 0) {
                        if (sleepgoal.getSelectedItemPosition() != 0) {

                            setSleepHours(bselH,bselM,wselH,wselM,
                                sleeplist.get(sleepgoal.getSelectedItemPosition()));

                        } else {
                            Toast.makeText(getActivity(),
                                "Please select the number of active hours.", Toast.LENGTH_SHORT)
                                .show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please select the wakeup time.",
                            Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getActivity(), "Please select the bed time.", Toast.LENGTH_SHORT)
                        .show();

                }
            }
        });





        return dialog;
    }
    private String getTime(int hr,int min) {
        Time tme = new Time(hr,min,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm");
        return formatter.format(tme);
    }
    private String getTimeAmPm(int hr,int min) {
        Time tme = new Time(hr,min,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("a");
        return formatter.format(tme);
    }
    public interface InterfaceCommunicator {
        void refresh();
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
    public interface InterfaceCommunicatorOtp {
        void  sendRequestCodeOtp();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        public SpinnerAdapter(Context context, int textViewResourceId,
            List<String> objects) {
            super(context, textViewResourceId, objects);

            // TODO Auto-generated constructor stub
        }

        @Override
        public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater=getActivity().getLayoutInflater();
            View row=inflater.inflate(R.layout.year_item, parent, false);
            TextView role=(TextView)row.findViewById(R.id.year);
            if(position==0)
                role.setTextColor(getResources().getColor(R.color.black15));
            else
                role.setTextColor(getResources().getColor(R.color.black));

            role.setText(sleeplist.get(position));

            return row;
        }
    }

    void setSleepHours(int bselH,int bselM,int wselH,int wselM,String sleephours)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar mcurrentTime = Calendar.getInstance();
        String bedTimeString=sdf.format(mcurrentTime.getTime())+"T"+getStringTime(bselH)+":"+getStringTime(bselM)+":00.000Z";
        mcurrentTime.add(Calendar.DAY_OF_MONTH,1);
        String wakeupTimeString=sdf.format(mcurrentTime.getTime())+"T"+getStringTime(wselH)+":"+getStringTime(wselM)+":00.000Z";

        SleepDurationPerDay sleepDurationPerDay=new SleepDurationPerDay(bedTimeString,wakeupTimeString,Integer.valueOf(sleephours));
        Goals goals=new Goals(sleepDurationPerDay);

        apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());
        com.fitkits.Model.User user_profile = new com.fitkits.Model.User(goals);

        apiService.updateProfile("/api/v1/cms/users/" + myPrefs.getString("user_id", ""), user_profile)
            .subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Observer<com.fitkits.Model.User>() {
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
                    userMasterGoal.getGoals().getSleepDurationPerDay().setValue(Integer.parseInt(sleephours));
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
    String getStringTime(int time){
        String timeString="";
        if(time<10){
            timeString="0"+String.valueOf(time);
        }
        else{
            timeString=String.valueOf(time);

        }
        return timeString;
    }
}



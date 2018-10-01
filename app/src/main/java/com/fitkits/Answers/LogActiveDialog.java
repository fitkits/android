package com.fitkits.Answers;

import static com.fitkits.Analytics.ActiveHours.ActiveHoursGraphActivity.activityGraphActivity;
import static com.fitkits.Home.Activities.HomeActivity.homeActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.Constants;
import com.fitkits.Model.ActivityAnswer;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import okhttp3.ResponseBody;

/**
 * Created by akshay on 10/07/17.
 */
public class LogActiveDialog extends BlurDialogFragment {

    Button done;
    Spinner activeHours;
    CardView activeCard;
    List<String> activeList=new ArrayList<String>();
    Realm realmUI;
    User userMasterGoal;
    SharedPreferences myPrefs;
    ActivityAnswer activityAnswer;
    String current;
    ProgressDialog progressDialog;
    ApiService apiService;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.log_active_hours);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        activeCard=(CardView) dialog.findViewById(R.id.activeCard);
        realmUI=Realm.getDefaultInstance();
        activeHours=(Spinner)dialog.findViewById(R.id.acitveHours);
        done=(Button) dialog.findViewById(R.id.next);

        activeList.add("XX");
        for(int i=1;i<25;i++){
            if(i<10)
                activeList.add("0"+String.valueOf(i));
            else
                activeList.add(String.valueOf(i));

        }
        activeHours.setAdapter(new SpinnerAdapter(getContext(),R.layout.year_item,activeList));


        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activeHours.getSelectedItemPosition()!=0) {

                    logActiveHours(Integer.parseInt(activeList.get(activeHours.getSelectedItemPosition())),"activePerDay");

                }

                else{
                    Toast.makeText(getActivity(),"Please select the number of active hours.",Toast.LENGTH_SHORT).show();
                }
            }
        });




        return dialog;
    }

    public interface InterfaceCommunicator {
        void  refresh();
        void refresh(int pos);
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

            role.setText(activeList.get(position));

            return row;
        }
    }

    void logActiveHours(int activehours,String type)  {
        try {

            String user = myPrefs.getString("user_id", "");
            SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
            sdf_new.setTimeZone(TimeZone.getTimeZone("IST"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            Calendar cal = Calendar.getInstance();
            current = sdf_new.format(cal.getTime());

            activityAnswer = new ActivityAnswer(user, type,activehours,current, 0);

            realmUI.beginTransaction();
            realmUI.copyToRealmOrUpdate(activityAnswer);
            realmUI.commitTransaction();


            if(Constants.isNetworkAvailable(getContext())){
                networkSync(activityAnswer);

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

    void networkSync(ActivityAnswer activityAnswer){
        ApiService apiService= RetroClient
            .getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());
        apiService.logActivity(activityAnswer)
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

                    ActivityAnswer activityAnswer1 = realmUI.where(ActivityAnswer.class).equalTo("date", current).findFirst();

                    realmUI.beginTransaction();
                    activityAnswer1.setServerLog(1);
                    realmUI.commitTransaction();
                    if (getArguments() != null) {
                        if(getArguments().getString("type").equals("home")) {
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) homeActivity;
                            interfaceCommunicator.refresh(3);
                            dismiss();
                        }
                        else{
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) activityGraphActivity;
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



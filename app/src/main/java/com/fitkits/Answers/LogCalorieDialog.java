package com.fitkits.Answers;

import static com.fitkits.Analytics.Calories.CalorieGraphActivity.calorieGraphActivity;
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
import com.fitkits.Model.CalorieAnswer;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import okhttp3.ResponseBody;

/**
 * Created by akshay on 10/07/17.
 */
public class LogCalorieDialog extends BlurDialogFragment {

    Button yes,no;
    Spinner activeHours;
    CardView activeCard;
    List<String> activeList=new ArrayList<String>();
    Realm realmUI;
    User userMasterGoal;
    SharedPreferences myPrefs;
    CalorieAnswer calorieAnswer;
    String current;
    ProgressDialog progressDialog;
    ApiService apiService;
    TextView time;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.log_calories);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        realmUI=Realm.getDefaultInstance();
        yes=(Button) dialog.findViewById(R.id.yes);
        no=(Button) dialog.findViewById(R.id.no);
        time=(TextView) dialog.findViewById(R.id.time);

        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        Calendar cal= Calendar.getInstance();
        time.setText(sdf.format(cal.getTime()));


        yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                    logCalorie("caloriesPerDay");

            }
        });

        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

    void logCalorie(String type)  {
        try {

            String user = myPrefs.getString("user_id", "");
            SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
            sdf_new.setTimeZone(TimeZone.getTimeZone("IST"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            Calendar cal = Calendar.getInstance();
            current = sdf_new.format(cal.getTime());
            User userMasterGoal = realmUI.where(User.class).findFirst();
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            float avgCal=Float.parseFloat(decimalFormat.format(userMasterGoal.getGoals().getCaloriesPerDay().getValue()/myPrefs.getInt("noofmeal",0)));

            calorieAnswer = realmUI.where(CalorieAnswer.class).equalTo("date", current).findFirst();

            float value=0;
            if(calorieAnswer!=null) {
                value=calorieAnswer.getValue()+avgCal;
            }
            else{
                value=avgCal;
            }


            calorieAnswer = new CalorieAnswer(user, type,Float.parseFloat(decimalFormat.format(value)),current, 0);

            realmUI.beginTransaction();
            realmUI.copyToRealmOrUpdate(calorieAnswer);
            realmUI.commitTransaction();
            calorieAnswer = new CalorieAnswer(user, type,avgCal,current, 0);


            if(Constants.isNetworkAvailable(getContext())){
                networkSync(calorieAnswer);

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

    void networkSync(CalorieAnswer calorieAnswer){
        ApiService apiService= RetroClient
            .getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());
        apiService.logCalorie(calorieAnswer)
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

                    CalorieAnswer calorieAnswer1 = realmUI.where(CalorieAnswer.class).equalTo("date", current).findFirst();

                    realmUI.beginTransaction();
                    calorieAnswer1.setServerLog(1);
                    realmUI.commitTransaction();
                    if (getArguments() != null) {
                        if(getArguments().getString("type").equals("home")) {
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) homeActivity;
                            interfaceCommunicator.refresh(5);
                            dismiss();
                        }
                        else{
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) calorieGraphActivity;
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



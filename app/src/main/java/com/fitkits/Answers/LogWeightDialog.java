package com.fitkits.Answers;

import static com.fitkits.Analytics.Weight.WeightGraphActivity.weightGraphActivity;
import static com.fitkits.GoalActivity.goalActivity;
import static com.fitkits.HomeActivity.homeActivity;

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
import com.fitkits.ApiService;
import com.fitkits.Constants;
import com.fitkits.CurrentWeight;
import com.fitkits.GoalDate;
import com.fitkits.GoalWeight;
import com.fitkits.Model.Weight;
import com.fitkits.Model.WeightAnswer;
import com.fitkits.R;
import com.fitkits.RecordedDate;
import com.fitkits.RetroClient;
import com.fitkits.User_;
import com.fitkits.WeightLog;
import com.fitkits.Weight_Data;
import com.fitkits.Weight_Goal_;
import com.fitkits.Weight_Response;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.TimeZone;
import okhttp3.ResponseBody;

/**
 * Created by akshay on 10/07/17.
 */
public class LogWeightDialog extends BlurDialogFragment {

    Button done;
    WeightLog weightLog;
    SharedPreferences myPrefs;
    TextView currentWeight,currentWeightDate;
    EditText goalWeight;
    TextView goalWeightDate;
    LinearLayout currentweightLay;

    ProgressDialog progressDialog;
CardView weightDateLay;
ApiService apiService;
WeightAnswer weightAnswer;
Realm realmUI;
String current;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.log_weight);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM   );
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        weightDateLay=(CardView)dialog.findViewById(R.id.weightDateLay);
        currentweightLay=(LinearLayout)dialog.findViewById(R.id.currentweightLay);
        currentWeight=(TextView) dialog.findViewById(R.id.currentWeight);
        goalWeightDate=(TextView) dialog.findViewById(R.id.weightDate);
        currentWeightDate=(TextView)dialog.findViewById(R.id.currentWeightDate);
        goalWeight=(EditText)dialog.findViewById(R.id.weight);
        Realm.init(getActivity());
        realmUI = Realm.getDefaultInstance();
        done=(Button) dialog.findViewById(R.id.next);


        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goalWeight.getText().toString().length()>0) {
                    if(goalWeightDate.getText().toString().length()>0) {

                                    logWeight(Integer.parseInt(goalWeight.getText().toString()),
                                        goalWeightDate.getText().toString(),"weight");
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
                weightDateLay.performClick();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
        try {

            currentWeight.setText(myPrefs.getString("recorded_weight", ""));

            currentWeightDate
                .setText(sdf_new.format(sdf.parse(myPrefs.getString("recorded_date", ""))));
        }
        catch (Exception e){

        }




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


    void logWeight(int weight,String goalDate,String type)  {
        try {

            String user = myPrefs.getString("user_id", "");
            SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
            sdf_new.setTimeZone(TimeZone.getTimeZone("IST"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            Calendar cal = Calendar.getInstance();
            current = sdf_new.format(cal.getTime());

                weightAnswer = new WeightAnswer(user, type, weight, sdf.format(sdf_new.parse(goalDate)), current, 0);

                realmUI.beginTransaction();
                realmUI.copyToRealmOrUpdate(weightAnswer);
                realmUI.commitTransaction();


                if(Constants.isNetworkAvailable(getContext())){
                    networkSync(weightAnswer);

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

    void networkSync(WeightAnswer weightAnswer){
        ApiService apiService= RetroClient
            .getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());

        apiService.logWeight(weightAnswer)
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

                    WeightAnswer weightAnswer1 = realmUI.where(WeightAnswer.class).equalTo("date", current).findFirst();

                    realmUI.beginTransaction();
                    weightAnswer1.setServerLog(1);
                    realmUI.commitTransaction();
                    if (getArguments() != null) {
                        if(getArguments().getString("type").equals("home")) {
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) homeActivity;
                            interfaceCommunicator.refresh(1);
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



package com.fitkits.Answers;

import static com.fitkits.Home.Activities.HomeActivity.homeActivity;
import static com.fitkits.Analytics.Water.WaterGraphActivity.waterGraphActivity;

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
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.Constants;
import com.fitkits.Model.WaterAnswer;
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
public class LogWaterDialog extends BlurDialogFragment {

    ProgressDialog progressDialog;
    Button done;
    TextView water,addGlass,subGlass;
    CardView waterCard,addWaterLay,subWaterLay;
    SharedPreferences myPrefs;
    List<String> waterList=new ArrayList<String>();
    Realm realmUI;
    String current;
    WaterAnswer waterAnswer;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.log_water);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        waterCard=(CardView) dialog.findViewById(R.id.waterCard);
        addWaterLay=(CardView) dialog.findViewById(R.id.addGlassLay);
        subWaterLay=(CardView) dialog.findViewById(R.id.subGlassLay);
        addGlass=(TextView) dialog.findViewById(R.id.addGlass);
        subGlass=(TextView) dialog.findViewById(R.id.subGlass);
        Realm.init(getActivity());
        realmUI = Realm.getDefaultInstance();
        water=(TextView) dialog.findViewById(R.id.water);
        done=(Button) dialog.findViewById(R.id.next);

       addWaterLay.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View view) {
               int glassCount=Integer.parseInt(water.getText().toString());
               glassCount=glassCount+1;
               if(glassCount>0){
                   subGlass.setTextColor(getResources().getColor(R.color.black));
               }
               else {
                   subGlass.setTextColor(getResources().getColor(R.color.black30));

               }
               water.setText(String.valueOf(glassCount));
           }
       });

        subWaterLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int glassCount=Integer.parseInt(water.getText().toString());
                if(glassCount>0) {
                    glassCount = glassCount - 1;
                    if (glassCount > 0) {
                        subGlass.setTextColor(getResources().getColor(R.color.black));
                    } else {
                        subGlass.setTextColor(getResources().getColor(R.color.black30));

                    }
                    water.setText(String.valueOf(glassCount));
                }
            }
        });
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(water.getText()!="0") {

                                logWater(water.getText().toString(),"waterConsumptionPerDay");


                }
                else{
                    Toast.makeText(getActivity(),"Please select the water intake amount.",Toast.LENGTH_SHORT).show();
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

            role.setText(waterList.get(position));

            return row;
        }
    }

    void logWater(String water,String type)  {

        try {
            String user=myPrefs.getString("user_id","");
            SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
            sdf_new.setTimeZone(TimeZone.getTimeZone("IST"));
            Calendar cal=Calendar.getInstance();
             current=sdf_new.format(cal.getTime());
            waterAnswer = realmUI.where(WaterAnswer.class).equalTo("date", current).findFirst();
            int value=0;
            if(waterAnswer!=null) {
                value=waterAnswer.getValue()+Integer.parseInt(water)*250;
            }
            else{
                value=Integer.parseInt(water)*250;
            }
            waterAnswer=new WaterAnswer(user,type,value,current,0);
            realmUI.beginTransaction();
            realmUI.copyToRealmOrUpdate(waterAnswer);
            realmUI.commitTransaction();
            waterAnswer=new WaterAnswer(user,type,Integer.parseInt(water)*250,current,0);


        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Constants.isNetworkAvailable(getContext())){
            networkSync(waterAnswer);

        }
        else{
            Constants.internetToastButLogged(getContext());
            dismiss();

        }

    }

    void networkSync(WaterAnswer waterAnswer){
        ApiService apiService= RetroClient
            .getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());
        apiService.logWater(waterAnswer)
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
                    WaterAnswer waterAnswer1 = realmUI.where(WaterAnswer.class).equalTo("date", current).findFirst();
                    realmUI.beginTransaction();
                    waterAnswer1.setServerLog(1);
                    realmUI.commitTransaction();

                    if (getArguments() != null) {
                        if(getArguments().getString("type").equals("home")) {
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) homeActivity;
                            interfaceCommunicator.refresh(2);
                            dismiss();
                        }
                        else{
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) waterGraphActivity;
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



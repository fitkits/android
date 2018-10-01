package com.fitkits.Dialogs;

import static com.fitkits.Goals.Activities.GoalActivity.goalActivity;
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
import com.fitkits.Model.*;
import com.fitkits.Model.User;
import com.fitkits.R;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.RetroClient;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10/07/17.
 */
public class WaterDialog extends BlurDialogFragment {

    ProgressDialog progressDialog;
    Button done;
    User userMasterGoal;
    Realm realmUI;
    SharedPreferences myPrefs;
    List<String> waterList=new ArrayList<String>();

    TextView water,addGlass,subGlass;
    CardView waterCard,addWaterLay,subWaterLay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.set_water);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        realmUI=Realm.getDefaultInstance();
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

        realmUI.beginTransaction();
        userMasterGoal = realmUI.where(User.class).findFirst();

        realmUI.commitTransaction();
        if(userMasterGoal!=null) {
            int glassCount=userMasterGoal.getGoals().getWaterConsumptionPerDay().getValue()/250;
            if(glassCount<10){
                water.setText("0"+String.valueOf(glassCount));
            }
            else{
                water.setText(String.valueOf(glassCount));

            }

        }
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
                if(glassCount<10){
                    water.setText("0"+String.valueOf(glassCount));
                }
                else{
                    water.setText(String.valueOf(glassCount));

                }
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
                    if(glassCount<10){
                        water.setText("0"+String.valueOf(glassCount));
                    }
                    else{
                        water.setText(String.valueOf(glassCount));

                    }
                }
            }
        });
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(water.getText().toString().length()>0) {
                            setWaterGoal(water.getText().toString());


                }
                else{
                    Toast.makeText(getActivity(),R.string.TOAST_INPUT_WATER_INTAKE,Toast.LENGTH_SHORT).show();
                }

            }
        });




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

            role.setText(waterList.get(position));

            return row;
        }
    }

    void setWaterGoal(String noc)  {

        ApiService apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());

            WaterConsumptionPerDay waterConsumptionPerDay=new WaterConsumptionPerDay(Integer.valueOf(noc)*250);
            Goals goals=new Goals(waterConsumptionPerDay);
            User user_profile = new User(goals);

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
                    userMasterGoal.getGoals().getWaterConsumptionPerDay().setValue(Integer.valueOf(water.getText().toString())*250);
                    realmUI.commitTransaction();
                    if(getArguments()!=null) {
                        if(getArguments().getString("type").equals("goal")) {
                            InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) goalActivity;
                            interfaceCommunicator.refresh();
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
                        R.string.TOAST_DEFAULT_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onComplete() {

                }
            });
    }

}



package com.fitkits.Dialogs;

import static com.fitkits.Analytics.Calories.CalorieGraphActivity.calorieGraphActivity;
import static com.fitkits.Goals.Activities.GoalActivity.goalActivity;

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
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10/07/17.
 */
public class CalorieDialog extends BlurDialogFragment {

    Button done;
    EditText calories;
    Spinner noofmeal;
    CardView noofmealsLay;
    List<String> mealList=new ArrayList<String>();
    Realm realmUI;
    User userMasterGoal;
    ApiService apiService;
    SharedPreferences myPrefs;
    ProgressDialog progressDialog;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.set_calories);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        realmUI=Realm.getDefaultInstance();
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        noofmealsLay=(CardView) dialog.findViewById(R.id.noofmealslay);

        noofmeal=(Spinner)dialog.findViewById(R.id.noofmeals);
        calories=(EditText) dialog.findViewById(R.id.calories);
        done=(Button) dialog.findViewById(R.id.next);
        realmUI.beginTransaction();
        userMasterGoal = realmUI.where(com.fitkits.Model.User.class).findFirst();

        realmUI.commitTransaction();

        noofmealsLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                noofmeal.performClick();
            }
        });

        mealList.add("XX");
        for(int i=1;i<11;i++){
            if(i!=10)
            mealList.add("0"+String.valueOf(i));
            else
                mealList.add(String.valueOf(i));

        }
        noofmeal.setAdapter(new SpinnerAdapter(getContext(),R.layout.year_item,mealList));
        if(userMasterGoal!=null) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            calories.setText(decimalFormat.format(userMasterGoal.getGoals().getCaloriesPerDay().getValue()));
            calories.setSelection(calories.getText().length());
            String ah="";
            int noofMeal=myPrefs.getInt("noofmeal",0);
            if(noofMeal!=0) {
                if (noofMeal < 10) {
                    ah = "0" + String.valueOf(noofMeal);
                } else {
                    ah = String.valueOf(noofMeal);
                }
                for (int i = 1; i < 11; i++) {
                    if (ah.equals(mealList.get(i))) {
                        noofmeal.setSelection(i);
                    }
                }
            }

        }
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calories.getText().toString().length()>0) {
                    if(noofmeal.getSelectedItemPosition()!=0) {

                        setCalorieGoal(Float.parseFloat(calories.getText().toString()));

                    }

                    else{
                        Toast.makeText(getActivity(),R.string.TOAST_SELECT_MEAL,Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),R.string.TOAST_SET_CALORIE_GOAL,Toast.LENGTH_SHORT).show();
                }
            }
        });





        return dialog;
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

            role.setText(mealList.get(position));

            return row;
        }
    }

    void setCalorieGoal(float calorieGoal)  {
        apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());
        CaloriesPerDay caloriesPerDay=new CaloriesPerDay(calorieGoal);
        Goals goal=new Goals(caloriesPerDay);
        User user_profile = new User(goal);

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
                    userMasterGoal.getGoals().getCaloriesPerDay().setValue(Float.parseFloat(calories.getText().toString()));
                    myPrefs.edit().putInt("noofmeal",Integer.parseInt(mealList.get(noofmeal.getSelectedItemPosition()))).commit();
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
                        R.string.TOAST_DEFAULT_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onComplete() {

                }
            });
    }


}



package com.fitkits.chart.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fitkits.Misc.Activities.Scores;
import com.fitkits.Misc.Activities.StudentAssessmentFragmentActivity;
import com.fitkits.Misc.Constants;
import com.fitkits.Misc.Fragments.StatsAssessmentFragment;
import com.fitkits.Model.User;
import com.fitkits.R;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.RetroClient;
import com.fitkits.Misc.Adapters.TransformationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyStatsActivity extends AppCompatActivity implements StatsAssessmentFragment.OnFragmentInteractionListener {
RecyclerView uploadRecyclerView;
CardView uploadPic;
Button close;
LinearLayout content;
CardView strength_container,strength_val_container,flex_container,flex_val_container,endurance_container,endurance_val_container;
TextView strength_val,flex_val,endurance_val;
ImageView strength_bar,endurance_bar,flex_bar;
ViewGroup.LayoutParams cont_params,bar_params;
LinearLayout.LayoutParams indic_params;
ProgressDialog progressDialog;
ApiService apiService;

SharedPreferences myPrefs;
  String value;
  boolean check = false;
  Fragment frg2;
  FragmentTransaction transaction;
    public static ArrayList<Scores> allAssesments = new ArrayList<>(1);

List<TransformationItem> transformationItemList=new ArrayList<TransformationItem>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_my_stats);
    LinearLayout back=(LinearLayout)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    content=(LinearLayout) findViewById(R.id.content);
    strength_bar=(ImageView)findViewById(R.id.strength_bar);
    strength_container=(CardView)findViewById(R.id.strength_container);
    strength_val_container=(CardView)findViewById(R.id.strength_val_container);
    strength_val=(TextView)findViewById(R.id.strength_val);
    endurance_bar=(ImageView)findViewById(R.id.endurance_bar);
    endurance_container=(CardView)findViewById(R.id.endurance_container);
    endurance_val_container=(CardView)findViewById(R.id.endurance_val_container);
    endurance_val=(TextView)findViewById(R.id.endurance_val);
    flex_bar=(ImageView)findViewById(R.id.flex_bar);
    flex_container=(CardView)findViewById(R.id.flex_container);
    flex_val_container=(CardView)findViewById(R.id.flex_val_container);
    flex_val=(TextView)findViewById(R.id.flex_val);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    frg2 = new StudentAssessmentFragmentActivity();



    if (Constants.isNetworkAvailable(this)) {
      getProfileDetail();
        getAssessments();

    } else {
      Toast.makeText(this, R.string.TOAST_NO_INETERNET, Toast.LENGTH_SHORT).show();

    }

  }
  public static float pxFromDp(final Context context, final float dp) {
    return dp * context.getResources().getDisplayMetrics().density;
  }

    public ArrayList<Scores> getAssessments() {
        apiService = RetroClient.getApiService(myPrefs.getString("token", ""), getApplicationContext());

        Log.e("RAM", "GETTING ASSESSMENT");
        allAssesments.clear();
        apiService.getAssessments(("/api/v1/cms/studentAssessments?user=" + myPrefs.getString("user_id", "") + "&page=1&perPageCount=2000")).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {

                try {
                    JSONObject rawAssessment = new JSONObject(value);
                    JSONArray assesment = rawAssessment.getJSONArray("StudentAssessments");
                    for(int i=0; i < assesment.length(); i++) {
                        Scores scores = new Scores("0", "0", "0","0", "0", "0", "");

                        try {
                            scores.balanceScore = assesment.getJSONObject(i).getJSONObject("data").getString("balanceScore");
                        } catch (Exception e) {

                        }
                        try {
                            scores.flexibilityScore = assesment.getJSONObject(i).getJSONObject("data").getString("flexibilityScore");
                        } catch (Exception e) {

                        }
                        try {
                            scores.strengthScore = assesment.getJSONObject(i).getJSONObject("data").getString("strengthScore");
                        } catch (Exception e) {

                        }
                        try {
                            scores.muscleScore = assesment.getJSONObject(i).getJSONObject("data").getString("muscleScore");
                        } catch (Exception e) {

                        }
                        try {
                            scores.cardioScore = assesment.getJSONObject(i).getJSONObject("data").getString("cardioScore");
                        } catch (Exception e) {

                        }
                        try {
                            scores.postureScore = assesment.getJSONObject(i).getJSONObject("data").getString("postureScore");
                        } catch (Exception e) {

                        }

//                        try {
//                            String dtStart =  assesment.getJSONObject(i).getString("timeStamp");;
//                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//                            try {
//                                Date date = format.parse(dtStart);
//                                android.text.format.DateFormat df = new android.text.format.DateFormat();
//                                scores.lastUpdated = (df.format("yyyy-MM-dd hh:mm:ss a", date));
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        } catch (Exception e) {
//
//                        }
                        try {
                            String[] tokens = (assesment.getJSONObject(i).getString("timeStamp")).split("T");
                            scores.lastUpdated = tokens[0];
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    allAssesments.add(scores);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                FragmentManager manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
//                Fragment frg = new StatsAssessmentFragment();
//                Bundle b = new Bundle();
                 Log.e("RAM",allAssesments.toString());
                transaction.add(R.id.assesment_fragment_container, frg2, "Frag_assessment_tag");
                transaction.commit();

            }
        });
        return  allAssesments;
    }

  void getProfileDetail(){
    apiService = RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());


    apiService.getUserProfile("/api/v1/cms/users/"+myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<com.fitkits.Model.User>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(MyStatsActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(com.fitkits.Model.User value) {
            content.setVisibility(View.VISIBLE);
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }

            if(value.getStrength().size()!=0) {
              Display display = getWindowManager().getDefaultDisplay();
              float width = display.getWidth() - pxFromDp(MyStatsActivity.this, 60);
              //cont_params = strenth_container.getLayoutParams();
              bar_params = strength_bar.getLayoutParams();
              bar_params.width = (int) (width * ((
                  value.getStrength().get(value.getStrength().size() - 1).getValue() / 100)));
              strength_bar.setLayoutParams(bar_params);
              strength_val.setText((int)(value.getStrength().get(value.getStrength().size() - 1).getValue())+"%");
              strength_container.setVisibility(View.VISIBLE);
              strength_val_container.setVisibility(View.VISIBLE);


            }
            else{
              strength_container.setVisibility(View.GONE);
              strength_val_container.setVisibility(View.GONE);
            }
            if(value.getFlex().size()!=0) {
              Display display = getWindowManager().getDefaultDisplay();
              float width = display.getWidth() - pxFromDp(MyStatsActivity.this, 60);
              //cont_params = strenth_container.getLayoutParams();
              bar_params = flex_bar.getLayoutParams();
              bar_params.width = (int) (width * ((
                  value.getFlex().get(value.getFlex().size() - 1).getValue() / 100)));
              flex_bar.setLayoutParams(bar_params);
              flex_val.setText((int)(value.getFlex().get(value.getFlex().size() - 1).getValue())+"%");
              flex_container.setVisibility(View.VISIBLE);
              flex_val_container.setVisibility(View.VISIBLE);

            }
            else{
              flex_container.setVisibility(View.GONE);
              flex_val_container.setVisibility(View.GONE);
            }
            if(value.getEndurance().size()!=0) {
              Display display = getWindowManager().getDefaultDisplay();
              float width = display.getWidth() - pxFromDp(MyStatsActivity.this, 60);
              //cont_params = strenth_container.getLayoutParams();
              bar_params = endurance_bar.getLayoutParams();
              bar_params.width = (int) (width * ((
                  value.getEndurance().get(value.getEndurance().size() - 1).getValue() / 100)));
              endurance_bar.setLayoutParams(bar_params);
              endurance_val.setText((int)(value.getEndurance().get(value.getEndurance().size() - 1).getValue())+"%");
              endurance_container.setVisibility(View.VISIBLE);
              endurance_val_container.setVisibility(View.VISIBLE);

            }
            else{
              endurance_container.setVisibility(View.GONE);
              endurance_val_container.setVisibility(View.GONE);
            }

          }

          @Override
          public void onError(Throwable e) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",e.getMessage());
            Toast.makeText(MyStatsActivity.this,R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });



  }

  @Override
    public void onFragmentInteraction(Uri uri) {
  }
}


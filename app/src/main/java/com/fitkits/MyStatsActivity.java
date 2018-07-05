package com.fitkits;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Model.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyStatsActivity extends AppCompatActivity {
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


    if (Constants.isNetworkAvailable(this)) {
      getProfileDetail();

    } else {
      Toast.makeText(this, "No Internet Connection Available! Please Check your Connection.", Toast.LENGTH_SHORT).show();

    }

  }
  public static float pxFromDp(final Context context, final float dp) {
    return dp * context.getResources().getDisplayMetrics().density;
  }
  void getProfileDetail(){
    apiService = RetroClient.getApiService(myPrefs.getString("mobileNumber",""),myPrefs.getString("otp",""),getApplicationContext());


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
            Toast.makeText(MyStatsActivity.this,"Something went wrong. Please try again later.",Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });


  }
}


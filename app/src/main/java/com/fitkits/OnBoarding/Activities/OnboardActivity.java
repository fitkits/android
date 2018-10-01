package com.fitkits.OnBoarding.Activities;

import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_1.flagfemale;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_1.flagmale;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_2.year;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_2.yearList;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_3.feet;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_3.inches;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_4.weight;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_5.flagNonVeg;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_5.flagVeg;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_6.bloodGroupList;
import static com.fitkits.OnBoarding.Fragments.OnBoardFragment_6.blood_group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.Pagers.CustomViewPager;
import com.fitkits.Model.User;
import com.fitkits.OnBoarding.Adapters.OnboardViewPagerAdapter;
import com.fitkits.R;
import com.fitkits.Misc.RetroClient;
import com.fitkits.Subscriptions.SubscriptionActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class OnboardActivity extends AppCompatActivity {
CustomViewPager onboard_vp;
public static Button back,next,done;
View line_break;
ProgressDialog progressDialog;
LinearLayout appbar;
SharedPreferences myPrefs;
public static LinearLayout disable_overlay;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboard);
    onboard_vp = (CustomViewPager) findViewById(R.id.onboard_vp);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    back=(Button)findViewById(R.id.back);
    next=(Button)findViewById(R.id.next);
    done=(Button)findViewById(R.id.done);
    appbar=(LinearLayout)findViewById(R.id.appbar);
    disable_overlay=(LinearLayout)findViewById(R.id.disable_overlay);
    disable_overlay.setVisibility(View.VISIBLE);
    next.setEnabled(false);
    back.setEnabled(false);
    done.setEnabled(false);
    line_break=(View)findViewById(R.id.line_break);
    onboard_vp.setOffscreenPageLimit(6);
    onboard_vp.setPagingEnabled(false);

    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        onboard_vp.setCurrentItem(onboard_vp.getCurrentItem()-1,true);
      }
    });
    next.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        onboard_vp.setCurrentItem(onboard_vp.getCurrentItem()+1,true);


      }
    });
    done.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        final ApiService apiService = RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());

        String gender="";
        String ht="";
        String foodPref="";

        if(flagmale)
          gender= "Male";
        if(flagfemale)
          gender= "Female";

        if(!inches.getText().toString().equals("")&&!feet.getText().toString().equals("")) {
          double in = Double.parseDouble(inches.getText().toString()) * 2.54;

          double ft = Double.parseDouble(feet.getText().toString()) * 30.48;
          double cm = in + ft;

          ht=String.valueOf(cm);
        }

        if(flagVeg)
          foodPref="VEG";
        if(flagNonVeg)
          foodPref="NON-VEG";
        //
        //User_Profile user_profile=new User_Profile(myPrefs.getString("name",""),bloodGroupList.get(blood_group.getSelectedItemPosition()),gender,yearList.get(year.getSelectedItemPosition())+"-01-01T00:00:01.000Z",ht,weight.getText().toString(),foodPref);
        com.fitkits.Model.User user_profile=new User(bloodGroupList.get(blood_group.getSelectedItemPosition()),gender,yearList.get(year.getSelectedItemPosition())+"-01-01T00:00:01.000Z",ht,weight.getText().toString(),foodPref);
        apiService.updateProfile("/api/v1/cms/users/"+myPrefs.getString("user_id",""),user_profile).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Observer<User>() {
              @Override
              public void onSubscribe(Disposable d) {
                progressDialog = new ProgressDialog(OnboardActivity.this);
                progressDialog.setMessage("Loading....");
                progressDialog.show();
                progressDialog.setCancelable(false);
              }

              @Override
              public void onNext(User value) {
                if(progressDialog.isShowing()&&this!=null){
                  progressDialog.dismiss();
                }

                try {
                  final Calendar myCalendar = Calendar.getInstance();
                  SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd'T'hh:mm:ss.SSS'Z'");
                  sdf.setTimeZone(TimeZone.getTimeZone("IST"));
                  myPrefs.edit().putString("user_id",value.getId()).commit();
                //  myPrefs.edit().putInt("recorded_weight",value.getWeight()).commit();
                  myPrefs.edit().putString("recorded_date",sdf.format(myCalendar.getTime())).commit();

                  Log.d("user_id",value.getId()+" "+myPrefs.getString("user_id",""));
                } catch (Exception e) {
                  e.printStackTrace();
                }
                Intent intent=new Intent(OnboardActivity.this, SubscriptionActivity.class);
                intent.putExtra("start","1");
                startActivity(intent);
                finish();
              }

              @Override
              public void onError(Throwable e) {
                if(progressDialog.isShowing()&&this!=null){
                  progressDialog.dismiss();
                }
                Toast.makeText(OnboardActivity.this,R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

              }

              @Override
              public void onComplete() {

              }
            });


       /* OkHttpClient client = new OkHttpClient();

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        JSONObject postdata = new JSONObject();
        try {
          postdata.put("name", myPrefs.getString("name",""));
          postdata.put("mobileNumber", myPrefs.getString("mobileNumber",""));
          postdata.put("bloodGroup", bloodGroupList.get(blood_group.getSelectedItemPosition()));
          if(flagmale)
          postdata.put("gender", "Male");
          if(flagfemale)
            postdata.put("gender", "Female");
          postdata.put("dob",yearList.get(year.getSelectedItemPosition())+"-01-01T00:00:01.000Z");
          if(!inches.getText().toString().equals("")&&!feet.getText().toString().equals("")) {
            double in = Double.parseDouble(inches.getText().toString()) * 2.54;

            double ft = Double.parseDouble(feet.getText().toString()) * 30.48;
            double cm = in + ft;

            postdata.put("height", String.valueOf(cm));
          }
          else{

          }
          postdata.put("weight", weight.getText().toString());
          if(flagVeg)
            postdata.put("foodPreference", "VEG");
          if(flagNonVeg)
            postdata.put("foodPreference", "NON-VEG");


        } catch(JSONException e){
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        final Request request = new Request.Builder()
            .url("http://139.59.80.139/cms/users/create")
            .post(body)
            .build();
        AnswerLog.d("request",postdata.toString());
        progressDialog = new ProgressDialog(OnboardActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        client.newCall(request).enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            Toast.makeText(OnboardActivity.this,R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_SHORT).show();

          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            String jsonData = response.body().string();
            AnswerLog.d("response",jsonData);

            try {
              JSONObject jsonObject = new JSONObject(jsonData);
              JSONObject data=jsonObject.getJSONObject("User");
              myPrefs.edit().putString("user_id",data.getString("_id")).commit();
            } catch (JSONException e) {
              e.printStackTrace();
            }
            startActivity(new Intent(OnboardActivity.this,GoalActivity.class));
            finish();
*/
          }

        });

    onboard_vp.setAdapter(new OnboardViewPagerAdapter(getSupportFragmentManager(), this));

    onboard_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        if(position==0){

          back.setVisibility(View.GONE);
          next.setVisibility(View.VISIBLE);
          done.setVisibility(View.GONE);
          line_break.setBackgroundColor(getResources().getColor(R.color.red));
          appbar.setVisibility(View.VISIBLE);
          if(flagmale||flagfemale){
            disable_overlay.setVisibility(View.GONE);
            next.setEnabled(true);
            back.setEnabled(true);
            done.setEnabled(true);
          }
          else{
            disable_overlay.setVisibility(View.VISIBLE);
            next.setEnabled(false);
            back.setEnabled(false);
            done.setEnabled(false);
          }
        }
        else if(position==1){
          if(year.getSelectedItemPosition()!=0){
            disable_overlay.setVisibility(View.GONE);
            next.setEnabled(true);
            back.setEnabled(true);
            done.setEnabled(true);
          }
          else{
            disable_overlay.setVisibility(View.VISIBLE);
            next.setEnabled(false);
            back.setEnabled(false);
            done.setEnabled(false);
          }
          back.setVisibility(View.VISIBLE);
          next.setVisibility(View.VISIBLE);
          done.setVisibility(View.GONE);
          appbar.setVisibility(View.VISIBLE);
          line_break.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if(position==2){
          if((inches.getText().length()>0)||feet.getText().length()>0){
            disable_overlay.setVisibility(View.GONE);
            next.setEnabled(true);
            back.setEnabled(true);
            done.setEnabled(true);
          }
          else{
            disable_overlay.setVisibility(View.VISIBLE);
            next.setEnabled(false);
            back.setEnabled(false);
            done.setEnabled(false);
          }
          back.setVisibility(View.VISIBLE);
          next.setVisibility(View.VISIBLE);
          done.setVisibility(View.GONE);
          appbar.setVisibility(View.VISIBLE);
          line_break.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if(position==3){
          if(weight.getText().length()>0){
            disable_overlay.setVisibility(View.GONE);
            next.setEnabled(true);
            back.setEnabled(true);
            done.setEnabled(true);
          }
          else {
            disable_overlay.setVisibility(View.VISIBLE);
            next.setEnabled(false);
            back.setEnabled(false);
            done.setEnabled(false);
          }
          back.setVisibility(View.VISIBLE);
          next.setVisibility(View.VISIBLE);
          done.setVisibility(View.GONE);
          appbar.setVisibility(View.VISIBLE);
          line_break.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if(position==4){
          if(flagVeg||flagNonVeg){
            disable_overlay.setVisibility(View.GONE);
            next.setEnabled(true);
            back.setEnabled(true);
            done.setEnabled(true);
          }
          else{
            disable_overlay.setVisibility(View.VISIBLE);
            next.setEnabled(false);
            back.setEnabled(false);
            done.setEnabled(false);
          }
          back.setVisibility(View.VISIBLE);
          next.setVisibility(View.VISIBLE);
          done.setVisibility(View.GONE);
          appbar.setVisibility(View.VISIBLE);
          line_break.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if(position==5){
          if(blood_group.getSelectedItemPosition()!=0){
            disable_overlay.setVisibility(View.GONE);
            next.setEnabled(true);
            back.setEnabled(true);
            done.setEnabled(true);
          }
          else{
            disable_overlay.setVisibility(View.VISIBLE);
            next.setEnabled(false);
            back.setEnabled(false);
            done.setEnabled(false);
          }
          back.setVisibility(View.VISIBLE);
          next.setVisibility(View.VISIBLE);
          done.setVisibility(View.GONE);
          appbar.setVisibility(View.VISIBLE);
          line_break.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else if(position==6){
          back.setVisibility(View.GONE);
          next.setVisibility(View.GONE);
          done.setVisibility(View.VISIBLE);
          appbar.setVisibility(View.GONE);
          line_break.setBackgroundColor(getResources().getColor(R.color.white));

        }
      }


      @Override
      public void onPageScrollStateChanged(int state) {

      }

    });
  }


}

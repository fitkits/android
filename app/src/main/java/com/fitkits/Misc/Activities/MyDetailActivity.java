package com.fitkits.Misc.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fitkits.Misc.Constants;
import com.fitkits.Misc.RetroClient;
import com.fitkits.Model.User;
import com.fitkits.R;
import com.fitkits.RealmObjects.ApiService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyDetailActivity extends AppCompatActivity {
EditText name,mobileNumber;
Button update;
ApiService apiService;
SharedPreferences myPrefs;
ProgressDialog progressDialog;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_my_details);
    name=(EditText)findViewById(R.id.name);
    mobileNumber=(EditText)findViewById(R.id.mobileNumber);
    update=(Button) findViewById(R.id.update);
    LinearLayout back=(LinearLayout)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    if (Constants.isNetworkAvailable(this)) {
      getProfileDetail();

    } else {
      Toast.makeText(this, R.string.TOAST_NO_INETERNET, Toast.LENGTH_SHORT).show();

    }

    update.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if(name.getText().length()>0){
          if(mobileNumber.getText().length()==10){
            if (Constants.isNetworkAvailable(MyDetailActivity.this)) {
              updateProfileDetail();

            } else {
              Toast.makeText(MyDetailActivity.this, R.string.TOAST_NO_INETERNET, Toast.LENGTH_SHORT).show();

            }
          }
          else{
            Toast.makeText(MyDetailActivity.this,R.string.TOAST_ENTER_VALID_MOBILE,Toast.LENGTH_SHORT).show();

          }
        }
        else{
          Toast.makeText(MyDetailActivity.this,R.string.TOAST_ENTER_NAME,Toast.LENGTH_SHORT).show();
        }
      }
    });


  }

  void getProfileDetail(){
    apiService = RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());


    apiService.getUserProfile("/api/v1/cms/users/"+myPrefs.getString("user_id","")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<User>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(MyDetailActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(User value) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }


            name.setText(value.getName());
            name.setSelection(name.getText().length());

            mobileNumber.setText(value.getMobileNumber().toString());

          }

          @Override
          public void onError(Throwable e) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",e.getMessage());
            Toast.makeText(MyDetailActivity.this,R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });


  }

  void updateProfileDetail(){

    User user_profile=new User(name.getText().toString(),Long.parseLong(mobileNumber.getText().toString()));

    apiService.updateProfile("/api/v1/cms/users/"+myPrefs.getString("user_id",""),user_profile).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<User>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(MyDetailActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(User value) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            myPrefs.edit().putString("user_id",value.getId()).commit();
            myPrefs.edit().putString("name",value.getName()).commit();

            myPrefs.edit().putString("mobileNumber",value.getMobileNumber().toString()).commit();
            finish();


          }

          @Override
          public void onError(Throwable e) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",e.getMessage());
            Toast.makeText(MyDetailActivity.this,R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });




  }


}

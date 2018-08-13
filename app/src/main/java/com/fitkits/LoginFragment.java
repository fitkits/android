package com.fitkits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.RegistrationDialog.InterfaceCommunicatorOtp;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment implements RegistrationDialog.InterfaceCommunicatorOtp,OtpDialog.InterfaceCommunicator {
CardView mobile_login;
ProgressDialog progressDialog;
public static LoginFragment loginFragment;
public static Button next;
EditText mobileNumber;
SharedPreferences myPrefs;

  public LoginFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.fragment_login, container, false);
    loginFragment=this;
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    next=(Button)view.findViewById(R.id.next);

    mobileNumber=(EditText)view.findViewById(R.id.mobileNumber);

    next.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if(mobileNumber.getText().length()==10) {
          sendOtp(mobileNumber.getText().toString());

        }
        else{
          Toast
              .makeText(getActivity(),"Please enter a valid 10 digit mobile number.",Toast.LENGTH_SHORT).show();
        }
      }
    });

    return view;
  }

  @Override
  public void sendRequestCodeOtp() {
    new Handler().postDelayed(new Runnable() {
      public void run() {
        new OtpDialog().show(getChildFragmentManager(),"");

      }
    }, 200);
  }

  public void sendOtp(String mobileNumber) {
    final ApiService apiService = RetroClient.getApiService("",getActivity().getApplicationContext());

    OTP user_ = new OTP(mobileNumber);
    apiService.sendOTP(user_).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
      @Override
      public void onSubscribe(Disposable d) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        progressDialog.setCancelable(false);
      }


      @Override
      public void onNext(ResponseBody responseBody) {
        if (progressDialog.isShowing() && this != null) {
          progressDialog.dismiss();
        }

        try {
          myPrefs.edit().putString("mobileNumber", mobileNumber).commit();

          InterfaceCommunicatorOtp interfaceCommunicatorOtp = (InterfaceCommunicatorOtp) loginFragment;
          interfaceCommunicatorOtp.sendRequestCodeOtp();

        } catch (Exception e) {
          e.printStackTrace();
        }

      }

      @Override
      public void onError(Throwable e) {
        if (progressDialog.isShowing() && this != null) {
          progressDialog.dismiss();
        }
        Toast.makeText(getActivity(), "Something went wrong. Please try again later.",
            Toast.LENGTH_LONG).show();

      }

      @Override
      public void onComplete() {

      }
    });
  }

  @Override
  public void subscribe() {
    Intent intent=new Intent(getActivity(), SubscriptionActivity.class);
    intent.putExtra("start","1");
    startActivity(intent);
  }

  @Override
  public void renew(long daysRemain) {
    RenewDialog renewDialog = new RenewDialog();
    if (daysRemain != 0) {
      Bundle bundle = new Bundle();
      bundle.putLong("daysRemain", daysRemain);
      bundle.putString("start","1");
      renewDialog.setArguments(bundle);
    }
    renewDialog.show(getFragmentManager(), "");
  }
}

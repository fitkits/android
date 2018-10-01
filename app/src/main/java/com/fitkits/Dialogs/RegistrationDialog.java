package com.fitkits.Dialogs;

import static com.fitkits.Auth.Fragments.LoginFragment.loginFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.fitkits.Auth.Beans.OTP;
import com.fitkits.R;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.RetroClient;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by akshay on 10/07/17.
 */
public class RegistrationDialog extends BlurDialogFragment {


    Button next;
    EditText name,mobileNumber;
    ProgressDialog progressDialog;
    SharedPreferences myPrefs;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(),R.style.MyDialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.reg_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


        next=(Button)dialog.findViewById(R.id.next);
        name=(EditText) dialog.findViewById(R.id.name);

        mobileNumber=(EditText)dialog.findViewById(R.id.mobileNumber);

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobileNumber.getText().length()==10) {
                    sendOtp(name.getText().toString(),mobileNumber.getText().toString());

                }
                else{
                    Toast.makeText(getActivity(),R.string.TOAST_ENTER_VALID_MOBILE,Toast.LENGTH_SHORT).show();
                }
            }
        });


        return dialog;
    }

    public void sendOtp(String name,String mobileNumber){
        final ApiService apiService = RetroClient.getApiService("",getActivity().getApplicationContext());

        OTP user_=new OTP(mobileNumber);
        apiService.sendOTP(user_).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable d) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                }


                @Override
                public void onNext(ResponseBody responseBody) {
                    if(progressDialog.isShowing()&&this!=null){
                        progressDialog.dismiss();
                    }

                    try {
                        myPrefs.edit().putString("name", name).commit();
                        myPrefs.edit().putString("mobileNumber", mobileNumber).commit();

                        InterfaceCommunicatorOtp interfaceCommunicatorOtp = (InterfaceCommunicatorOtp) loginFragment;
                        interfaceCommunicatorOtp.sendRequestCodeOtp();
                        dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Throwable e) {
                    if(progressDialog.isShowing()&&this!=null){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getActivity(),R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

                }

                @Override
                public void onComplete() {

                }
            });
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



}



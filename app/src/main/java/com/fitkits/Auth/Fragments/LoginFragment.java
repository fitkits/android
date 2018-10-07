package com.fitkits.Auth.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Auth.Beans.OTP;
import com.fitkits.Auth.Dialogs.OtpDialog;
import com.fitkits.R;
import com.fitkits.Dialogs.RegistrationDialog.InterfaceCommunicatorOtp;
import com.fitkits.Dialogs.RenewDialog;
import com.fitkits.Misc.RetroClient;
import com.fitkits.Subscriptions.SubscriptionActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * ================
 * Raghu's Comment
 * ================
 *
 * This Fragment is made for Login and OTP verification.
 * Entry points of it are in LoginActivity. Please refer
 * LoginActivity.java.
 */
public class LoginFragment extends Fragment implements InterfaceCommunicatorOtp, OtpDialog.InterfaceCommunicator {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginFragment = this;
        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        next = (Button) view.findViewById(R.id.next);

        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);

        /**
         * The Next button Click Listener
         */
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Check for proper mobile number
                 */
                if (mobileNumber.getText().length() == 10) {
                    /**
                     * This function handles the API request to
                     * ask the backend to send an OTP. Go inside
                     * its definition for more info.
                     */
                    sendOtp(mobileNumber.getText().toString());

                } else {
                    Toast.makeText(getActivity(), R.string.TOAST_ENTER_VALID_MOBILE, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void sendRequestCodeOtp() {
        new Handler().postDelayed(new Runnable() {
            /**
             * Here we open the OTPDialog to ask the
             * user to input the OTP. Go to OTPDialog.java
             * to proceed with the flow.
             */
            public void run() {
                new OtpDialog().show(getChildFragmentManager(), "");

            }
        }, 200);
    }

    public void sendOtp(String mobileNumber) {
        final ApiService apiService = RetroClient.getApiService("", getActivity().getApplicationContext());

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
                    /**
                     * Once we send the SMS successfully, the backend respond with
                     * a success message and the number it sent the message to.
                     * We store it in perfs for future use.
                     */
                    myPrefs.edit().putString("mobileNumber", mobileNumber).commit();

                    /**
                     * the sendRequestCodeOTP is a poorly named function. it basically just
                     * does VerifyOTP API call and some logic.
                     * I didn't renamed it because I'm still not aware of the
                     * full codebase and don't want to break any dependencies.
                     *
                     * GO TO sendReuqestCodeOTP function from here.
                     *
                     * NOTE: dont control click sendRequestOTP, it will take
                     * you to the interface definition. This method has been
                     * overridden here. so CTRL+F for it.
                     */
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
                Toast.makeText(getActivity(), R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void subscribe() {
        Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
        intent.putExtra("start", "1");
        startActivity(intent);
    }

    @Override
    public void renew(long daysRemain) {
        RenewDialog renewDialog = new RenewDialog();
        if (daysRemain != 0) {
            Bundle bundle = new Bundle();
            bundle.putLong("daysRemain", daysRemain);
            bundle.putString("start", "1");
            renewDialog.setArguments(bundle);
        }
        renewDialog.show(getFragmentManager(), "");
    }
}

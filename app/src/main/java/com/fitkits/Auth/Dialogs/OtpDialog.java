package com.fitkits.Auth.Dialogs;

import static com.fitkits.Auth.Fragments.LoginFragment.loginFragment;
import static com.fitkits.Auth.Fragments.LoginFragment.next;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Auth.Beans.OTP;
import com.fitkits.Model.*;
import com.fitkits.OnBoarding.Activities.OnboardActivity;
import com.fitkits.R;
import com.fitkits.Misc.RetroClient;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by akshay on 10/07/17.
 *
 * ======================
 * Raghu's Comment
 * ======================
 *
 * This Dialog Fragment has loads of business logic inside it.
 * Which is one of the primary reason we can't refactor the codebase
 * much. GO TO onCreateDialog.
 */
public class OtpDialog extends BlurDialogFragment {

    Button verify;
    ProgressDialog progressDialog;
    TextView resend, mobileText;
    EditText et1, et2, et3, et4;
    SharedPreferences myPrefs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());

        configureDialog(dialog);
        configureOTPEditTexts(dialog);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mobileText.setText("Enter OTP received on " + myPrefs.getString("mobileNumber", ""));

        verify = (Button) dialog.findViewById(R.id.verify);

        resend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                next.performClick();
            }
        });
        verify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP = validateOTP();
                verifyOtp(OTP);
            }
        });
        return dialog;
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

    public interface InterfaceCommunicatorSaveBio {
        void sendRequestCodeSaveBio(String bio);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private String validateOTP() {
        String[] OTPDigits  = { et1.getText().toString(),
                et2.getText().toString(),
                et3.getText().toString(),
                et4.getText().toString()};
        boolean OTPDigitsAllValid = false;
        for (int i = 0; i < OTPDigits.length; i++) {
            if (OTPDigits[i].length() <= 0) {
                OTPDigitsAllValid = false;
            } else {
                OTPDigitsAllValid = true;
            }
        }
        if (OTPDigitsAllValid) {
            return  TextUtils.join("",OTPDigits);


        } else {
            Toast.makeText(getActivity(), "Please enter the OTP.", Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    public void verifyOtp(String otp) {
        final ApiService apiService = RetroClient.getApiService("", getActivity().getApplicationContext());


        OTP userOtp = new OTP(myPrefs.getString("mobileNumber", ""), otp);
        apiService.verifyOtp(userOtp).subscribeOn(
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
                    public void onNext(com.fitkits.Model.User value) {
                        if (progressDialog.isShowing() && this != null) {
                            progressDialog.dismiss();
                        }

                        try {
                            myPrefs.edit().putString("user_id", value.getId()).commit();
                            myPrefs.edit().putString("otp", otp).commit();
                            myPrefs.edit().putString("token", "Bearer " + value.getJwt()).commit();
                            dismiss();
                            /**
                             * We do the same Check We did for LoginActivity.
                             * We make sure we got all the details of the user before
                             * we take them inside our app.
                             */
                            boolean isHeightNull = (value.getHeight() == null);
                            boolean isWeightNull = (value.getWeight() == null);
                            boolean isFoodPreferenceNull  = (value.getFoodPreference() == null);
                            boolean isBloodGroupNull  = (value.getBloodGroup() == null);
                            if(isHeightNull || isWeightNull || isFoodPreferenceNull || isBloodGroupNull) {

                                startActivity(new Intent(getContext(), OnboardActivity.class));
                                getActivity().finish();
                            }
                           else {
                                getRenewDetail();
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing() && this != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), R.string.TOAST_DEFAULT_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    void getRenewDetail() {
//        Toast.makeText(getContext(),"INSIDE OTP DIALOG",Toast.LENGTH_LONG).show();
        ApiService apiService = RetroClient
                .getApiService(myPrefs.getString("token", ""),
                        getActivity().getApplicationContext());

        apiService.getSubscriptions("/api/v1/cms/subscriptions?user=" + myPrefs.getString("user_id", "")).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<ItemParent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Loading....");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                    }

                    @Override
                    public void onNext(ItemParent itemParent) {
                        if (progressDialog.isShowing() && this != null) {
                            progressDialog.dismiss();
                        }
                        List<Membership> value = itemParent.getSubscriptions();
                        Log.d("Response", value.toString());
                        try {
                            long daysRemain = 0;
                            if (value.size() != 0) {
                                Membership membership = value.get(value.size() - 1);
                                String dt = membership.getEndDate();
                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                                Calendar ex = Calendar.getInstance();
                                try {
                                    ex.setTime(sdf.parse(dt));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String currentDate = sdf.format(new Date());
                                Calendar cu = Calendar.getInstance();
                                double diff = ex.getTimeInMillis() - cu.getTimeInMillis();
                                double days = diff / (24 * 60 * 60 * 1000);
                                daysRemain = Math.round(days);


                            }
                            if (value.size() != 0) {
                                if (daysRemain > 0) {
                                    InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) loginFragment;
                                    interfaceCommunicator.renew(daysRemain);
                                    dismiss();
                                } else {
                                    InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) loginFragment;
                                    interfaceCommunicator.subscribe();
                                    dismiss();

                                }
                            } else {
                                InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) loginFragment;
                                interfaceCommunicator.subscribe();
                                dismiss();


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing() && this != null) {
                            progressDialog.dismiss();
                        }
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getActivity(), R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                                Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void configureDialog(Dialog dialog) {
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.set_weight);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(R.layout.otp_dialog);

        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        resend = (TextView) dialog.findViewById(R.id.resend);
        mobileText = (TextView) dialog.findViewById(R.id.mobileText);
    }

    /**
     * This method adds listeners and configures the Editboxes for Checks.
     * There is too much redundant code, mostly due to the listeners. I
     * seperated this code from the OnCreate for some readablity, but
     * Still have to find a way to reduce the code.
     * @param dialog
     */
    private  void  configureOTPEditTexts(Dialog dialog) {
        et1 = (EditText) dialog.findViewById(R.id.et1);
        et2 = (EditText) dialog.findViewById(R.id.et2);
        et3 = (EditText) dialog.findViewById(R.id.et3);
        et4 = (EditText) dialog.findViewById(R.id.et4);

        et1.requestFocus();

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et1.getText().toString().length() == 1)     //size as per your requirement
                {
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et2.getText().toString().length() == 1)     //size as per your requirement
                {
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et3.getText().toString().length() == 1)     //size as per your requirement
                {
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et1.getText().length() > 0 && et2.getText().length() > 0 && et3.getText().length() > 0 && et4.getText().length() > 0) {
                    verifyOtp(et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString());

                } else {
                    Toast.makeText(getActivity(), "Please enter the OTP.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public interface InterfaceCommunicator {
        void subscribe();

        void renew(long daysRemain);
    }


}



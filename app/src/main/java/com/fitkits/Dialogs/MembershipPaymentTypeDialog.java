package com.fitkits.Dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fitkits.Membership.MembershipAdapter;
import com.fitkits.Membership.MembershipItem;
import com.fitkits.Misc.PendingInterface;
import com.fitkits.Misc.RetroClient;
import com.fitkits.Model.Membership;
import com.fitkits.Model.PendingMembership;
import com.fitkits.Model.User;
import com.fitkits.R;
import com.fitkits.RealmObjects.ApiService;

import org.w3c.dom.Text;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MembershipPaymentTypeDialog extends Dialog {

    public Activity context;
    public Dialog d;
    public Button offline, online;
    public Switch acceptTerms;
    public  List<MembershipItem> membershipItemList;
    public PendingInterface pendingInterface;
    public TextView termsText;
    SharedPreferences myPrefs;
    boolean ispending = true;
    ProgressDialog progressDialog;
    public int position;

    public MembershipPaymentTypeDialog(Activity a, int style, List<MembershipItem> membershipItemList,
                                       int position, PendingInterface pendingInterface) {
        super(a, style);
        this.context = a;
        this.membershipItemList = membershipItemList;
        this.position = position;
        this.pendingInterface = pendingInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.membership_payment_dialog);
        offline = (Button) findViewById(R.id.button_offline);
        online = (Button) findViewById(R.id.button_online);
        acceptTerms = (Switch)findViewById(R.id.accept_terms);
        termsText = (TextView)findViewById(R.id.terms_text);
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        offline.setEnabled(false);
        online.setEnabled(false);

        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.faqURL)));
                context.startActivity(browserIntent);
            }
        });

                online.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        MembershipAdapter.InterfaceCommunicator interfaceCommunicator = (MembershipAdapter.InterfaceCommunicator) context;
                        interfaceCommunicator.sendRequestCodePay(position, membershipItemList.get(position).getId(), (membershipItemList.get(position).getCost()));

                    }
                });

                offline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String membershipId = membershipItemList.get(position).getId();
                        //PendingMembership pendingMembership1 = new PendingMembership(membershipId, ispending);
                        ApiService apiService = RetroClient
                                .getApiService(myPrefs.getString("token", ""), context);
                        //  getdetails();
                        PendingMembership pendingMembership1 = new PendingMembership(membershipId, ispending);
                        User u = new User();
                        u.setPendingMembership(pendingMembership1);
                        apiService.update_PendingMembership("/api/v1/cms/users/" + myPrefs.getString("user_id", ""), u)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<User>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                progressDialog = new ProgressDialog(context);
                                progressDialog.setMessage("Loading....");
                                progressDialog.show();
                                progressDialog.setCancelable(false);

                            }

                            @Override
                            public void onNext(User value) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                                pendingInterface.passActivity();
                            }

                            @Override
                            public void onError(Throwable e) {
                                // Toast.makeText(getApplicationContext(),"Please select the number of active hours.",Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(context, R.string.TOAST_DEFAULT_ERROR_MESSAGE,
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                        //Post the data and move to pending


                    }

                });

        acceptTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    online.setEnabled(true);
                    offline.setEnabled(true);
                }
                else {
                    online.setEnabled(false);
                    offline.setEnabled(false);

                }
            }
        });
    }


}
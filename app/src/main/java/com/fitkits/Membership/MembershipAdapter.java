package com.fitkits.Membership;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fitkits.Dialogs.MembershipPaymentTypeDialog;
import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.Constants;
import com.fitkits.Misc.Constants.PicassoTrustAll;
import com.fitkits.Model.PendingMembership;
import com.fitkits.Model.User;
import com.fitkits.Misc.PendingInterface;
import com.fitkits.R;
import com.fitkits.Misc.RetroClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.ShowViewHolder> {
    List<MembershipItem> membershipItemList = new ArrayList<MembershipItem>();
    Activity context;
    View view;
    String membershipId = "";
    boolean ispending = true;
    PendingMembership pendingMembership1;
    public PendingInterface pendingInterface;
    int selectedPos = -1;
    SharedPreferences myPrefs;

    ProgressDialog progressDialog;
    android.support.v4.app.FragmentManager fragmentManager;

    public MembershipAdapter(PendingInterface pendingInterface, android.support.v4.app.FragmentManager fragmentManager, Activity context, List<MembershipItem> membershipItemList
    ) {
        this.membershipItemList = membershipItemList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        this.pendingInterface = pendingInterface;


    }


    @Override
    public MembershipAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.membership_item_even, parent, false);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.membership_item_odd, parent, false);
        }
        MembershipAdapter.ShowViewHolder showViewHolder = new MembershipAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final MembershipAdapter.ShowViewHolder holder, final int position) {

        PicassoTrustAll.getInstance(context).load(Constants.base_url + membershipItemList.get(position).getImageURL()).into(holder.membershipIcon);
        holder.membershipTitle.setText(membershipItemList.get(position).getName());
        float amount = ((membershipItemList.get(position).getCost()) / 100);
        holder.membershipPrice.setText(String.valueOf(amount));

        if (((membershipItemList.get(position).getExpiryDays()) / 30) >= 12) {
            int year = (int) ((membershipItemList.get(position).getExpiryDays()) / 30) / 12;
            if (year == 1) {
                holder.membershipValidity.setText(String.valueOf(year) + " Year");
            } else {
                holder.membershipValidity.setText(String.valueOf(year) + " Years");

            }

        } else {
            int months = (int) ((membershipItemList.get(position).getExpiryDays()) / 30);
            if (months == 1) {
                holder.membershipValidity.setText(String.valueOf(months) + " Month");
            } else {
                holder.membershipValidity.setText(String.valueOf(months) + " Months");

            }

        }
        holder.membershipType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                membershipId = membershipItemList.get(position).getId();
                MembershipPaymentTypeDialog cdd = new MembershipPaymentTypeDialog(context,R.style.Theme_AppCompat_Light_Dialog_Alert,membershipItemList,position, pendingInterface);
                cdd.show();
//                AlertDialog.Builder alert = new AlertDialog.Builder(context);
//                alert.setTitle("Payment Method");
//                alert.setMessage("How do you want to pay?");
//                alert.setPositiveButton("Online", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) context;
//                        interfaceCommunicator.sendRequestCodePay(position, membershipItemList.get(position).getId(), (membershipItemList.get(position).getCost()));
//
//                    }
//                });
//                alert.setNegativeButton("Offline", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //PendingMembership pendingMembership1 = new PendingMembership(membershipId, ispending);
//                        ApiService apiService = RetroClient
//                                .getApiService(myPrefs.getString("token", ""), context);
//                        //  getdetails();
//                        PendingMembership pendingMembership1 = new PendingMembership(membershipId, ispending);
//                        User u = new User();
//                        u.setPendingMembership(pendingMembership1);
//                        apiService.update_PendingMembership("/api/v1/cms/users/" + myPrefs.getString("user_id", ""), u).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<User>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//                                progressDialog = new ProgressDialog(context);
//                                progressDialog.setMessage("Loading....");
//                                progressDialog.show();
//                                progressDialog.setCancelable(false);
//
//                            }
//
//                            @Override
//                            public void onNext(User value) {
//                                if (progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
//
//                                pendingInterface.passActivity();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                // Toast.makeText(getApplicationContext(),"Please select the number of active hours.",Toast.LENGTH_SHORT).show();
//                                if (progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
//                                Toast.makeText(context, R.string.TOAST_DEFAULT_ERROR_MESSAGE,
//                                        Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//                        //Post the data and move to pending
//
//
//                    }
//                });
//                alert.show();

            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 2 == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return membershipItemList.size();
    }


    /**
     * Initiates Payment through RazorPay.
     */

    /**
     * Class for declaring the class variables for the showtime recycler view items.
     */
    public class ShowViewHolder extends RecyclerView.ViewHolder {

        ImageView membershipIcon;
        TextView membershipTitle, membershipPrice, membershipValidity;
        RelativeLayout membershipType;


        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         *
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);
            membershipIcon = (ImageView) itemView.findViewById(R.id.membershipIcon);

            membershipTitle = (TextView) itemView.findViewById(R.id.membershipTitle);

            membershipPrice = (TextView) itemView.findViewById(R.id.membershipPrice);

            membershipValidity = (TextView) itemView.findViewById(R.id.membershipValidity);

            membershipType = (RelativeLayout) itemView.findViewById(R.id.membershipType);


        }
    }

//    public void getdetails() {
//        ApiService apiService = RetroClient
//                .getApiService(myPrefs.getString("mobileNumber", ""), myPrefs.getString("otp", ""),
//                        context);
//        apiService.getPendingMembership("/api/v1/cms/users/" + myPrefs.getString("user_id", "")).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<User>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                progressDialog = new ProgressDialog(context);
//                progressDialog.setMessage("Loading....");
//                progressDialog.show();
//                progressDialog.setCancelable(false);
//
//            }
//
//            @Override
//            public void onNext(User value) {
//                if (progressDialog.isShowing() && this != null) {
//                    progressDialog.dismiss();
//                }
//                ispending = "true";
//                // membershipId = value.getPendingMembership().getMembership().toString();
//
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                // Toast.makeText(getApplicationContext(),"Please select the number of active hours.",Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//
//    }


    public interface InterfaceCommunicator {
        void sendRequestCodePay(int selectedPos, String membershipId, int price);
    }
}

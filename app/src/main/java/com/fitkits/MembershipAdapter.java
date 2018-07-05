package com.fitkits;

import static android.content.ContentValues.TAG;
import static com.fitkits.LoginFragment.loginFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Constants.PicassoTrustAll;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.ShowViewHolder>  {
    List<MembershipItem> membershipItemList=new ArrayList<MembershipItem>();
    Activity context;
    View view;
    String membershipId="";
    int selectedPos=-1;
    SharedPreferences myPrefs;
    ProgressDialog progressDialog;
    android.support.v4.app.FragmentManager fragmentManager;

    public MembershipAdapter(android.support.v4.app.FragmentManager fragmentManager,Activity context, List<MembershipItem> membershipItemList) {
        this.membershipItemList=membershipItemList;
        this.context=context;
        this.fragmentManager=fragmentManager;
        myPrefs= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());


    }


    @Override
    public MembershipAdapter.ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0) {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.membership_item_even, parent, false);
        }
        else if(viewType==1){
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.membership_item_odd, parent, false);
        }
        MembershipAdapter.ShowViewHolder showViewHolder = new MembershipAdapter.ShowViewHolder(view);
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(final MembershipAdapter.ShowViewHolder holder, final int position) {

        PicassoTrustAll.getInstance(context).load(Constants.base_url+membershipItemList.get(position).getImageURL()).into(holder.membershipIcon);
        holder.membershipTitle.setText(membershipItemList.get(position).getName());
        float amount=((membershipItemList.get(position).getCost())/100);
        holder.membershipPrice.setText(String.valueOf(amount));

        if(((membershipItemList.get(position).getExpiryDays())/30)>=12){
            int year=(int)((membershipItemList.get(position).getExpiryDays())/30)/12;
            if(year==1) {
                holder.membershipValidity.setText(String.valueOf(year) + " Year");
            }
            else{
                holder.membershipValidity.setText(String.valueOf(year) + " Years");

            }

        }
        else{
            int months=(int)((membershipItemList.get(position).getExpiryDays())/30);
            if(months==1) {
                holder.membershipValidity.setText(String.valueOf(months) + " Month");
            }
            else{
                holder.membershipValidity.setText(String.valueOf(months) + " Months");

            }

        }
        holder.membershipType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InterfaceCommunicator interfaceCommunicator = (InterfaceCommunicator) context;
                interfaceCommunicator.sendRequestCodePay(position,membershipItemList.get(position).getId(),(membershipItemList.get(position).getCost()));
            }
        });





    }

    @Override
    public int getItemViewType(int position) {
        if((position+1)%2==0)
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
        TextView membershipTitle,membershipPrice,membershipValidity;
        RelativeLayout membershipType;



        /**
         * Class constructor for binding the class variables with the corresponding views of the layout.
         * @param itemView Parent view in the corresponding layout.
         */
        public ShowViewHolder(View itemView) {
            super(itemView);
            membershipIcon=(ImageView)itemView.findViewById(R.id.membershipIcon);

            membershipTitle=(TextView) itemView.findViewById(R.id.membershipTitle);

            membershipPrice=(TextView)itemView.findViewById(R.id.membershipPrice);

            membershipValidity=(TextView)itemView.findViewById(R.id.membershipValidity);

            membershipType=(RelativeLayout) itemView.findViewById(R.id.membershipType);




        }
    }



    public interface InterfaceCommunicator {
        void  sendRequestCodePay(int selectedPos,String membershipId,int price);
    }
}
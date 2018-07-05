package com.fitkits;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.Membership;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import org.reactivestreams.Subscription;


public class SubscriptionActivity extends AppCompatActivity implements PaymentResultListener ,MembershipAdapter.InterfaceCommunicator{
ProgressDialog progressDialog;
ApiService apiService;
RecyclerView membershipRecyclerView;
SharedPreferences myPrefs;
String membershipId="";
int selectedPos=-1;
int amount=0;
List<MembershipItem> membershipItemList=new ArrayList<MembershipItem>();
    @Override
    public void onBackPressed() {

            Toast.makeText(this,"please select the subsription ",Toast.LENGTH_SHORT);

    }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subscription);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    membershipRecyclerView=(RecyclerView) findViewById(R.id.membershipRecyclerView);
    membershipRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    LinearLayout back=(LinearLayout)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
          Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);

      }
    });
    if (Constants.isNetworkAvailable(this)) {
      getMembershipList();

    } else {
      Toast.makeText(this, "No Internet Connection Available! Please Check your Connection.", Toast.LENGTH_SHORT).show();

    }

  }

public void getMembershipList(){
   apiService=RetroClient.getApiService(myPrefs.getString("mobileNumber",""),myPrefs.getString("otp",""),getApplicationContext());

  apiService.getMemberships().subscribeOn(
      Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
      new Observer<ItemParent>() {
        @Override
        public void onSubscribe(Disposable d) {
          progressDialog = new ProgressDialog(SubscriptionActivity.this);
          progressDialog.setMessage("Loading....");
          progressDialog.show();
          progressDialog.setCancelable(false);
        }

        @Override
        public void onNext(ItemParent value) {

          if(progressDialog.isShowing()&&this!=null){
            progressDialog.dismiss();
          }


          membershipItemList=value.getMembership();
          if(membershipItemList.size()>0){
            membershipRecyclerView.setAdapter(new MembershipAdapter(getSupportFragmentManager(),SubscriptionActivity.this,membershipItemList));
            membershipRecyclerView.setVisibility(View.VISIBLE);
          }



        }

        @Override
        public void onError(Throwable e) {
          if(progressDialog.isShowing()&&this!=null){
            progressDialog.dismiss();
          }
          Log.d("Response",e.getMessage());
          Toast.makeText(SubscriptionActivity.this,"Something went wrong. Please try again later.",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onComplete() {

        }
      });



  /*OkHttpClient client = new OkHttpClient();

  final Request request = new Request.Builder()
      .url("http://139.59.80.139/cms/memberships/")
      .get()
      .build();
  progressDialog = new ProgressDialog(SubscriptionActivity.this);
  progressDialog.setMessage("Loading....");
  progressDialog.show();
  progressDialog.setCancelable(false);
  client.newCall(request).enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
      Toast.makeText(SubscriptionActivity.this,"Something went wrong. Please try again later.",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
      if(progressDialog.isShowing()&&this!=null){
        progressDialog.dismiss();
      }
      String jsonData = response.body().string();
      try {
        final JSONArray data = new JSONArray(jsonData);
        membershipItemList.clear();
        for(int i=0;i<data.length();i++){
          membershipItemList.add(new MembershipItem(data.getJSONObject(i).getString("_id"),data.getJSONObject(i).getString("name"),"",data.getJSONObject(i).getString("cost"),data.getJSONObject(i).getString("expiryDays")));

        }

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            //Handle UI here
            try {
              if(membershipItemList.size()>0){
                membershipRecyclerView.getAdapter().notifyDataSetChanged();
                membershipRecyclerView.setVisibility(View.VISIBLE);
              }
            }
            catch (Exception e) {
              e.printStackTrace();
            }            }
        });




      } catch (JSONException e) {
        e.printStackTrace();
      }

    }
  });*/

}

  void  renewMembership(final int selectedPos,String membershipId,String paymentId) {

    Membership membership = new Membership(myPrefs.getString("user_id",""),membershipId,paymentId);

    apiService.subscribe(membership)
        .subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(SubscriptionActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(ResponseBody value) {

            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }



            SubscribedDialog subscribedDialog = new SubscribedDialog();
            Bundle bundle = new Bundle();
            bundle.putString("title", membershipItemList.get(selectedPos).getName());
            bundle.putString("price", membershipItemList.get(selectedPos).getCost().toString());
            bundle.putString("iconUrl", membershipItemList.get(selectedPos).getImageURL());
            bundle.putString("validity", membershipItemList.get(selectedPos).getExpiryDays().toString());
            if(getIntent()!=null&&getIntent().getStringExtra("start").equals("1"))
              bundle.putString("start","1");

            subscribedDialog.setArguments(bundle);
            subscribedDialog.show(getSupportFragmentManager(), "");


          }

          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(SubscriptionActivity.this,
                "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });
  }

  void capturePayment(final int selectedPos,String membershipId,String paymentId,int amount){


    Payment payment=new Payment(paymentId,amount);

    apiService.capturePayment(payment).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Payment>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(SubscriptionActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(Payment value) {

            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }

            renewMembership(selectedPos,membershipId,value.getId());


          }

          @Override
          public void onError(Throwable e) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",e.getMessage());
            Toast.makeText(SubscriptionActivity.this,"Something went wrong. Please try again later.",Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });
/*

    OkHttpClient client = new OkHttpClient();

    MediaType MEDIA_TYPE = MediaType.parse("application/json");
    JSONObject postdata = new JSONObject();
    try {
      JSONArray membershipArray=new JSONArray();
      JSONObject jsonObject0 = new JSONObject();
      JSONObject membership=new JSONObject();
      if(!membershipId.equals(""))
        membership.put("_id",membershipId);
      jsonObject0.put("membership",membership);
      membershipArray.put(jsonObject0);
      postdata.put("memberships", membershipArray);



    } catch(JSONException e){
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    AnswerLog.d("request",postdata.toString());
    RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

    final Request request = new Request.Builder()
        .url("http://139.59.80.139/cms/users/"+myPrefs.getString("user_id",""))
        .patch(body)
        .build();
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading....");
    progressDialog.show();
    progressDialog.setCancelable(false);
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        Toast.makeText(SubscriptionActivity.this,"Something went wrong. Please try again later.",Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if(progressDialog.isShowing()&&this!=null){
          progressDialog.dismiss();
        }
        String jsonData = response.body().string();
        try {
          JSONObject jsonObject = new JSONObject(jsonData);
          AnswerLog.d("response",jsonData);
          JSONObject data=jsonObject.getJSONObject("User");
          myPrefs.edit().putString("user_id",data.getString("_id")).commit();
          myPrefs.edit().putString("name",data.getString("name")).commit();

          myPrefs.edit().putString("mobileNumber",data.getString("mobileNumber")).commit();
          if(progressDialog.isShowing()&&this!=null){
            progressDialog.dismiss();
          }
          SubscribedDialog subscribedDialog=new SubscribedDialog();
          Bundle bundle=new Bundle();
          bundle.putString("title",membershipItemList.get(selectedPos).getTitle());
          bundle.putString("price",membershipItemList.get(selectedPos).getPrice());
          bundle.putString("iconUrl",membershipItemList.get(selectedPos).getIconUrl());
          bundle.putString("validity",membershipItemList.get(selectedPos).getValidity());


          subscribedDialog.setArguments(bundle);
          subscribedDialog.show(getSupportFragmentManager(),"");


        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
    });*/
  }

  @Override
  public void sendRequestCodePay(int selectedPos, String membershipId, int price) {
    startPayment(selectedPos,membershipId,price);

  }
  public void startPayment(int selectedPos,String membershipId,int price ) {
    /**
     * You need to pass current activity in order to let Razorpay create CheckoutActivity
     */
    this.selectedPos=selectedPos;
    this.membershipId=membershipId;
    this.amount=price;
    final Activity activity = this;

    final Checkout co = new Checkout();

    try {
      JSONObject options = new JSONObject();
      options.put("name", "FitKits");
      options.put("description", "Order #1");
      //You can omit the image option to fetch the image from dashboard
      options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
      options.put("currency", "INR");
      options.put("amount", price);


      JSONObject preFill = new JSONObject();
      preFill.put("email", "user@fitkits.com");
      preFill.put("contact", "9876543210");

      options.put("prefill", preFill);

      co.open(activity, options);
    } catch (Exception e) {
      Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
          .show();
      e.printStackTrace();
    }
  }

  /**
   * The name of the function has to be
   * onPaymentSuccess
   * Wrap your code in try catch, as shown, to ensure that this method runs correctly
   */
  @SuppressWarnings("unused")


  @Override
  public void onPaymentSuccess(String s) {

    if(!membershipId.equals("")&&selectedPos!=-1&&amount!=0){
      capturePayment(selectedPos,membershipId,s,amount);
      //renewMembership(selectedPos,membershipId,s,amount);
    }
  }

  @Override
  public void onPaymentError(int i, String s) {
    try {
      Toast.makeText(this, "Unable to connect.Try again later.", Toast.LENGTH_SHORT).show();
      Log.d("Payment Failed:", s);
    } catch (Exception e) {
      Log.e(TAG, "Exception in onPaymentError", e);
    }
  }

}

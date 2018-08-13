package com.fitkits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.Membership;
import com.fitkits.Model.User;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatUser;

import org.reactivestreams.Subscription;

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
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends Fragment {
ProgressDialog progressDialog;
  public ProfileFragment() {
  }
ApiService apiService;
  LinearLayout myStats,logout,expirse_layoyt,freshchat;
  TextView loggedInAs,expirse_date;
  long daysRemain;
  SharedPreferences myPrefs;
  CustomViewPager banner;
  LinearLayout mydetail,mytransformation,linkmedia;
  Button renew;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.fragment_profile, container, false);
    expirse_layoyt = (LinearLayout) view.findViewById(R.id.Expires_layout);
    freshchat=(LinearLayout)view.findViewById(R.id.freshchat);
    expirse_date = (TextView) view.findViewById(R.id.expire_date);
    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.appbar);
    TextView day = (TextView) toolbar.findViewById(R.id.toolbar_day);
    TextView date = (TextView) toolbar.findViewById(R.id.toolbar_date);
    TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
    loggedInAs = (TextView)view.findViewById(R.id.loggedInAs);
    myStats = (LinearLayout) view.findViewById(R.id.mystats);
    logout = (LinearLayout)view.findViewById(R.id.logout);

    ImageView dropdown = (ImageView) toolbar.findViewById(R.id.toolbar_dropdown);
    date.setVisibility(View.GONE);
    day.setVisibility(View.GONE);
    dropdown.setVisibility(View.GONE);
    title.setText("Profile");
    title.setVisibility(View.VISIBLE);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    mydetail=(LinearLayout)view.findViewById(R.id.mydetails);
    linkmedia=(LinearLayout)view.findViewById(R.id.linkmedia);
    mytransformation=(LinearLayout)view.findViewById(R.id.mytransformation);
    renew=(Button)view.findViewById(R.id.renew);
    getRenewDetail1();

    mydetail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(),MyDetailActivity.class));
      }
    });
    mytransformation.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(),MyTransformationActivity.class));
      }
    });
    myStats.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(),MyStatsActivity.class));

      }
    });
    freshchat.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        //init
        FreshchatConfig freshchatConfig=new FreshchatConfig(
                "ec6cc751-ca88-4c03-9a10-ab06f857d0ed","6dbb69d9-b7cc-4ce5-b8aa-881c00827cbb");
        Freshchat.getInstance(getActivity()).init(freshchatConfig);



        apiService = RetroClient.getApiService(myPrefs.getString("token", ""),getActivity());


        apiService.getUserProfile("/api/v1/cms/users/"+myPrefs.getString("user_id","")).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<com.fitkits.Model.User>() {
                  @Override
                  public void onSubscribe(Disposable d) {

                  }

                  @Override
                  public void onNext(User value) {
//
                      FreshchatUser user = Freshchat.getInstance(getActivity()).getUser();
                      user.setFirstName(""+value.getName()).setPhone("0091", ""+value.getMobileNumber());
                      Freshchat.getInstance(getActivity()).setUser(user);




                  }

                  @Override
                  public void onError(Throwable e) {
//                    if(progressDialog.isShowing()&&this!=null){
//                      progressDialog.dismiss();
//                    }
                    Log.d("Response",e.getMessage());
                   // Toast.makeText(MyDetailActivity.this,"Something went wrong. Please try again later.",Toast.LENGTH_LONG).show();

                  }

                  @Override
                  public void onComplete() {

                  }
                });
        Freshchat.showConversations(getActivity());



      }
    });
    logout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        myPrefs.edit().clear().commit();
        startActivity(new Intent(getActivity(),LoginActivity.class));
        getActivity().finish();
      }
    });
    loggedInAs.setText("Logged in as "+myPrefs.getString("mobileNumber",""));
    renew.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (Constants.isNetworkAvailable(getActivity())) {
          getRenewDetail();

        } else {
          Toast.makeText(getActivity(), "No Internet Connection Available! Please Check your Connection.", Toast.LENGTH_SHORT).show();

        }
      }
    });

    linkmedia.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(),LinkSocialMediaActivity.class));

      }
    });

    return view;
  }
  void getRenewDetail(){


    ApiService apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());

    apiService.getSubscriptions("/api/v1/cms/subscriptions?user="+myPrefs.getString("user_id","")).subscribeOn(
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
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }

            List<Membership> value=itemParent.getSubscriptions();

            Log.d("Response",value.toString());
            try {
              long daysRemain=0;
                if(value.size()!=0){
                  Membership membership=value.get(value.size()-1);
                  String dt = membership.getEndDate();
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                  Calendar ex = Calendar.getInstance();
                  try {
                    ex.setTime(sdf.parse(dt));
                  } catch (ParseException e) {
                    e.printStackTrace();
                  }
                  String currentDate = sdf.format(new Date());
                  Calendar cu=Calendar.getInstance();
                  double diff=ex.getTimeInMillis()-cu.getTimeInMillis();
                  double days = diff / (24 * 60 * 60 * 1000);
                  daysRemain=Math.round(days);



                }
              if(value.size()!=0){
                if (daysRemain> 0) {
                  RenewDialog renewDialog = new RenewDialog();
                  Bundle bundle = new Bundle();
                  bundle.putLong("daysRemain", daysRemain);
                  bundle.putString("start", "0");
                  renewDialog.setArguments(bundle);
                  renewDialog.show(getChildFragmentManager(), "");
                }
                else {
                  startActivity(new Intent(getActivity(),SubscriptionActivity.class));

                }
              }
              else{
                startActivity(new Intent(getActivity(),SubscriptionActivity.class));

              }

            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          @Override
          public void onError(Throwable e) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",e.getMessage());
            Toast.makeText(getActivity(),"Something went wrong. Please try again later3.",Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });





/*   OkHttpClient client = new OkHttpClient();



    final Request request = new Request.Builder().url("http://139.59.80.139/cms/users/"+myPrefs.getString("user_id","")).get(.build();
    progressDialog = new ProgressDialog(getActivity());
    progressDialog.setMessage("Loading....");
    progressDialog.show();
    progressDialog.setCancelable(false);
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        Toast.makeText(getActivity(),"Something went wrong. Please try again later.",Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if(progressDialog.isShowing()&&this!=null){
          progressDialog.dismiss();
        }
        String jsonData = response.body().string();
        AnswerLog.d("response",jsonData);
        try {
          final JSONObject data = new JSONObject(jsonData);
          getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //Handle UI here
              try {
                JSONArray jsonArray=data.getJSONArray("memberships");
                long daysRemain=0;
                for(int i=0;i<jsonArray.length();i++){
                  if(jsonArray.getJSONObject(i).getBoolean("expired")==false){
                    String dt = jsonArray.getJSONObject(i).getString("startDate");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                    Calendar ex = Calendar.getInstance();
                    try {
                      ex.setTime(sdf.parse(dt));
                    } catch (ParseException e) {
                      e.printStackTrace();
                    }
                    ex.add(Calendar.DAY_OF_MONTH, jsonArray.getJSONObject(i).getJSONObject("membership").getInt("expiryDays")+1+jsonArray.getJSONObject(i).getInt("carryOverDays"));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                    String currentDate = sdf.format(new Date());
                    Calendar cu=Calendar.getInstance();
                    long diff=ex.getTimeInMillis()-cu.getTimeInMillis();
                    daysRemain = diff / (24 * 60 * 60 * 1000);


                  }
                }
                if(jsonArray.length()!=0) {
                  RenewDialog renewDialog = new RenewDialog();
                  if (daysRemain != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("daysRemain", daysRemain);
                    renewDialog.setArguments(bundle);
                  }
                  renewDialog.show(getChildFragmentManager(), "");
                }
                else{
                  startActivity(new Intent(getActivity(),SubscriptionActivity.class));

                }

              } catch (Exception e) {
                e.printStackTrace();
              }            }
          });


        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
    });*/
  }
  void getRenewDetail1() {


    ApiService apiService = RetroClient.getApiService(myPrefs.getString("token", ""), getActivity().getApplicationContext());

    apiService.getSubscriptions("/api/v1/cms/subscriptions?user=" + myPrefs.getString("user_id", "")).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            new Observer<ItemParent>() {
              @Override
              public void onSubscribe(Disposable d) {
              }

              @Override
              public void onNext(ItemParent itemParent) {
                List<Membership> value = itemParent.getSubscriptions();
                try {
                  daysRemain = 0;
                  Membership membership = value.get(value.size() - 1);
                  String dt = membership.getEndDate();
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
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
                  if (daysRemain <7) {
                    expirse_layoyt.setVisibility(View.VISIBLE);
                    expirse_date.setText("");
                    expirse_date.setText("" + daysRemain + " days Subscription remaining");
                  }

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onError(Throwable e) {
                Toast.makeText(getActivity(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
              }
              @Override
              public void onComplete() {
              }
            });
  }

}

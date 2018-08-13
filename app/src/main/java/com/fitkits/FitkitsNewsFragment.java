package com.fitkits;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fitkits.Model.Feed;
import com.fitkits.Model.ItemParent;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FitkitsNewsFragment extends Fragment {
ProgressDialog progressDialog;
SharedPreferences myPrefs;

  public FitkitsNewsFragment() {
  }
  RecyclerView fitkits_news_recycleview;
  List<Feed> fitkitsNewsItemList=new ArrayList<Feed>();
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.fragment_fitkits_news, container, false);
    Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.appbar);
    TextView day = (TextView) toolbar.findViewById(R.id.toolbar_day);
    TextView date = (TextView) toolbar.findViewById(R.id.toolbar_date);
    TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

    ImageView dropdown = (ImageView) toolbar.findViewById(R.id.toolbar_dropdown);
    date.setVisibility(View.GONE);
    day.setVisibility(View.GONE);
    dropdown.setVisibility(View.GONE);
    title.setText("Fitkits News");
    title.setVisibility(View.VISIBLE);
    fitkits_news_recycleview=(RecyclerView)view.findViewById(R.id.fitkits_news_recycleview);
    fitkits_news_recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
    if (Constants.isNetworkAvailable(getContext())) {
      loadBlogList();

    } else {
      Toast.makeText(getContext(), "No Internet Connection Available! Please Check your Connection.", Toast.LENGTH_SHORT).show();

    }



    return view;
  }
  void loadBlogList(){
    ApiService apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());

    apiService.getFeeds().subscribeOn(
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
            List<Feed> value=itemParent.getFeeds();


            fitkitsNewsItemList=value;
            if(fitkitsNewsItemList.size()>0){
              fitkits_news_recycleview.setAdapter(new FitkitsNewsAdapter(getActivity(),fitkitsNewsItemList));
              fitkits_news_recycleview.setVisibility(View.VISIBLE);
            }



          }

          @Override
          public void onError(Throwable e) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",e.getMessage());
            Toast.makeText(getActivity(),"Something went wrong. Please try again later.",Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });





/*

    OkHttpClient client = new OkHttpClient();



    final Request request = new Request.Builder()
        .url("http://139.59.80.139/cms/feeds/")
        .get()
        .build();
    progressDialog = new ProgressDialog(getContext());
    progressDialog.setMessage("Loading....");
    progressDialog.show();
    progressDialog.setCancelable(false);
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        Toast.makeText(getContext(),"Something went wrong. Please try again later.",Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if(progressDialog.isShowing()&&this!=null){
          progressDialog.dismiss();
        }
        String jsonData = response.body().string();
        try {
          final JSONArray data = new JSONArray(jsonData);
          fitkitsNewsItemList.clear();
          for(int i=0;i<data.length();i++){
            fitkitsNewsItemList.add(new FitkitsNewsItem(data.getJSONObject(i).getString("_id"),data.getJSONObject(i).getString("title"),"",""));

          }

          getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //Handle UI here
              try {
                if(fitkitsNewsItemList.size()>0){
                  fitkits_news_recycleview.getAdapter().notifyDataSetChanged();
                  fitkits_news_recycleview.setVisibility(View.VISIBLE);
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
    });
    */
  }

}

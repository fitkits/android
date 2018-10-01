package com.fitkits.News.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fitkits.RealmObjects.ApiService;
import com.fitkits.Misc.Constants;
import com.fitkits.Misc.Constants.PicassoTrustAll;
import com.fitkits.Model.Feed;
import com.fitkits.R;
import com.fitkits.Misc.RetroClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsDetailsActivity extends AppCompatActivity {
  ImageView uploadImage;
  Bitmap bitmap;
  Button upload;
  TextView title,description,author,postDate,tags;
  ProgressDialog progressDialog;
  RelativeLayout content;
  SharedPreferences myPrefs;
  ImageView blog_image;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fitkits_news_detail);
    title=(TextView)findViewById(R.id.blogTitle);
    description=(TextView)findViewById(R.id.description);
    author=(TextView)findViewById(R.id.author);
    postDate=(TextView)findViewById(R.id.postDate);
    tags=(TextView)findViewById(R.id.tags);
    content=(RelativeLayout) findViewById(R.id.content);
    blog_image=(ImageView)findViewById(R.id.blog_image);
    ImageView back=(ImageView)findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    myPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    if (Constants.isNetworkAvailable(this)) {
      if(getIntent()!=null)
      loadBlogDetail(getIntent().getStringExtra("blog_id"));

    } else {
      Toast.makeText(this, R.string.TOAST_NO_INETERNET, Toast.LENGTH_SHORT).show();

    }


  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setResult(101);
  }

  void loadBlogDetail(String blog_id){
ApiService apiService=RetroClient.getApiService(myPrefs.getString("token", ""),getApplicationContext());
    apiService.getFeedDetail("/api/v1/cms/feeds/"+blog_id).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<Feed>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(NewsDetailsActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(Feed value) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",value.toString());
try {
  title.setText(value.getTitle());
  description.setText(Html.fromHtml(value.getDescription()));
  author.setText(value.getAuthor());
  if(value.getImageURL()!=null){


    PicassoTrustAll.getInstance(getApplicationContext()).load(Constants.base_url+value.getImageURL()).into(blog_image);
  }
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
  Date fdate = sdf.parse(value.getPostDate());
  SimpleDateFormat dayf = new SimpleDateFormat("dd");
  SimpleDateFormat monthf = new SimpleDateFormat("MMM");
  SimpleDateFormat yearf = new SimpleDateFormat("yyyy");

  postDate.setText(dayf.format(fdate) + "th " + monthf.format(fdate) + " " + yearf.format(fdate));
  List<String> tagsList = value.getTags();
  String tagString = "";
  for (int i = 0; i < tagsList.size(); i++) {
    if (i != tagsList.size() - 1) {
      tagString = tagString + tagsList.get(i) + ", ";
    } else {
      tagString = tagString + tagsList.get(i);
    }
  }
  tags.setText(tagString);
  content.setVisibility(View.VISIBLE);
}
catch (Exception e){

}

          }

          @Override
          public void onError(Throwable e) {
            if(progressDialog.isShowing()&&this!=null){
              progressDialog.dismiss();
            }
            Log.d("Response",e.getMessage());
            Toast.makeText(NewsDetailsActivity.this,R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });


    /*


    OkHttpClient client = new OkHttpClient();



    final Request request = new Request.Builder()
        .url("http://139.59.80.139/cms/feeds/"+blog_id)
        .get()
        .build();
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading....");
    progressDialog.show();
    progressDialog.setCancelable(false);
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        Toast.makeText(NewsDetailsActivity.this,R.string.TOAST_DEFAULT_ERROR_MESSAGE,Toast.LENGTH_SHORT).show();

      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if(progressDialog.isShowing()&&this!=null){
          progressDialog.dismiss();
        }
        final String jsonData = response.body().string();
        try {
          final JSONObject data = new JSONObject(jsonData);
          NewsDetailsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //Handle UI here
              try {
                AnswerLog.d("response",jsonData);
                title.setText(data.getString("title"));
                description.setText(data.getString("description"));
                author.setText(data.getString("author"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                Date fdate=sdf.parse(data.getString("postDate"));
                SimpleDateFormat dayf = new SimpleDateFormat("dd");
                SimpleDateFormat monthf = new SimpleDateFormat("MMM");
                SimpleDateFormat yearf = new SimpleDateFormat("yyyy");

                postDate.setText(dayf.format(fdate)+"th "+monthf.format(fdate)+" "+yearf.format(fdate));
                JSONArray tagsList=data.getJSONArray("tags");
                String tagString="";
                for(int i=0;i<tagsList.length();i++){
                  if(i!=tagsList.length()-1){
                    tagString=tagString+tagsList.getString(i)+", ";
                  }
                  else{
                    tagString=tagString+tagsList.getString(i);
                  }
                }
                tags.setText(tagString);
                content.setVisibility(View.VISIBLE);

              } catch (JSONException e) {
                e.printStackTrace();
              } catch (ParseException e) {
                e.printStackTrace();
              }
            }
          });


        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
    });*/
  }
}

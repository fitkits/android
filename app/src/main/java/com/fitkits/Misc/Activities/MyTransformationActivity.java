package com.fitkits.Misc.Activities;

import android.Manifest.permission;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fitkits.Misc.CameraOptionsDialog;
import com.fitkits.Misc.RetroClient;
import com.fitkits.Misc.Adapters.TransformationAdapter;
import com.fitkits.Model.*;
import com.fitkits.R;
import com.fitkits.RealmObjects.ApiService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyTransformationActivity extends AppCompatActivity {

  RecyclerView uploadRecyclerView;
  CardView uploadPic;
  Button close;
  ApiService apiService;
  public Uri selectedImage;
  ProgressDialog progressDialog;
  SharedPreferences myPrefs;
  public  static android.support.v4.app.FragmentManager fragmentManager;
  List<Stats> transformationItemList = new ArrayList<Stats>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_my_tranformation);
    uploadRecyclerView = (RecyclerView) findViewById(R.id.uploadRecyclerView);
    uploadPic = (CardView) findViewById(R.id.uploadPic);
    close = (Button) findViewById(R.id.close);
    fragmentManager=getSupportFragmentManager();
    uploadRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    LinearLayout back = (LinearLayout) findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    uploadPic.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if ((ContextCompat.checkSelfPermission(MyTransformationActivity.this,
            permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)) {

          ActivityCompat.requestPermissions(MyTransformationActivity.this,
              new String[]{ permission.WRITE_EXTERNAL_STORAGE},
              1);
        } else {
         new CameraOptionsDialog().show(getSupportFragmentManager(),"");

        }


      }
    });
    close.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    getProfileDetail();

  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1888 && resultCode == Activity.RESULT_OK && data != null) {
      Bitmap photo = (Bitmap) data.getExtras().get("data");
      //imageView.setImageBitmap(photo);
      Intent intent = new Intent(MyTransformationActivity.this, PreviewActivity.class);
      ByteArrayOutputStream bStream = new ByteArrayOutputStream();
      photo.compress(Bitmap.CompressFormat.PNG, 100, bStream);
      byte[] byteArray = bStream.toByteArray();
      // String path = Images.Media.insertImage(getContentResolver(), photo, "Title", null);

      intent.putExtra("imageBitmap", byteArray);
      selectedImage = data.getData();
      Uri tempUri = getImageUri(getApplicationContext(), photo);
      String path=getRealPathFromURI(tempUri);
      intent.putExtra("uri", path);

      startActivityForResult(intent, 100);
    } else if (requestCode == 1889 && resultCode == Activity.RESULT_OK && data != null) {
      try {
        Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

        //imageView.setImageBitmap(photo);
        Intent intent = new Intent(MyTransformationActivity.this, PreviewActivity.class);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bStream);

        byte[] byteArray = bStream.toByteArray();
        intent.putExtra("imageBitmap", byteArray);
         selectedImage = data.getData();
        Uri tempUri = getImageUri(getApplicationContext(), photo);
        String path=getRealPathFromURI(tempUri);
        intent.putExtra("uri", path);


        startActivityForResult(intent, 100);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
      String path=data.getExtras().getString("uri");
      updateProfileDetail(path);

    }
  }


  void getProfileDetail() {
    apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""),
            getApplicationContext());
    apiService.getUserProfile("/api/v1/cms/users/" + myPrefs.getString("user_id", "")).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<com.fitkits.Model.User>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(MyTransformationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(com.fitkits.Model.User value) {
            //content.setVisibility(View.VISIBLE);
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }

            if(value.getImages().size()!=0) {
              List<Stats> images=value.getImages();
              uploadRecyclerView.setAdapter(new TransformationAdapter(MyTransformationActivity.this,images));



            }


          }

          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(MyTransformationActivity.this,
                R.string.TOAST_DEFAULT_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });


  }
  public String getRealPathFromURI(Uri uri) {
    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    cursor.moveToFirst();
    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
    return cursor.getString(idx);
  }
  public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
  }
  void updateProfileDetail(String selectedImage) {
    apiService = RetroClient
        .getApiService(myPrefs.getString("token", ""),
            getApplicationContext());
    File file = new File(selectedImage);
    Log.d("URI", "Filename " + file.getName());

    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
    MultipartBody.Part fileToUpload = MultipartBody.Part
        .createFormData("image", file.getName(), mFile);

    apiService
        .updateProfileImage("/api/v1/cms/users/" + myPrefs.getString("user_id", ""), fileToUpload)
        .subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        new Observer<ResponseBody>() {
          @Override
          public void onSubscribe(Disposable d) {
            progressDialog = new ProgressDialog(MyTransformationActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            progressDialog.setCancelable(false);
          }

          @Override
          public void onNext(ResponseBody value) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            getProfileDetail();

          }

          @Override
          public void onError(Throwable e) {
            if (progressDialog.isShowing() && this != null) {
              progressDialog.dismiss();
            }
            Log.d("Response", e.getMessage());
            Toast.makeText(MyTransformationActivity.this,
                R.string.TOAST_DEFAULT_ERROR_MESSAGE, Toast.LENGTH_LONG).show();

          }

          @Override
          public void onComplete() {

          }
        });
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case 1: {
        // If request is cancelled, the result arrays are empty.
        if((grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          // permission granted and now can proceed
          CameraOptionsDialog cameraOptionsDialog = new CameraOptionsDialog();
          getSupportFragmentManager().beginTransaction().add(cameraOptionsDialog, "options").commitAllowingStateLoss();
        } else {

          // permission denied, boo! Disable the
          // functionality that depends on this permission.
          Toast.makeText(MyTransformationActivity.this, R.string.TOAST_EXTENRAL_STORAGE_PERMISSION_DENIED, Toast.LENGTH_SHORT).show();
        }
        return;
      }
      // add other cases for more permissions
    }
  }
}


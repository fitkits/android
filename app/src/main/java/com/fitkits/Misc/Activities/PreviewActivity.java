package com.fitkits.Misc.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitkits.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PreviewActivity extends AppCompatActivity {
  ImageView uploadImage;
  Bitmap bitmap;
  Button upload;
  TextView transformDate;
  EditText currentWeight;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preview_transform);
    uploadImage=(ImageView)findViewById(R.id.uploadImage);
    upload=(Button) findViewById(R.id.upload);
    transformDate=(TextView) findViewById(R.id.transformDate);
    currentWeight = (EditText)findViewById(R.id.currentWeight);
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Calendar calendar=Calendar.getInstance();
    transformDate.setText(sdf.format(calendar.getTime()));


    final byte[] byteArray =  getIntent().getByteArrayExtra("imageBitmap");
    String uri= getIntent().getStringExtra("uri");
    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    uploadImage.setImageBitmap(bitmap);
    upload.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent=new Intent();
        intent.putExtra("imageBitmap",byteArray);
        intent.putExtra("currentWeight",currentWeight.getText().toString());
        intent.putExtra("uri",uri);

        setResult(RESULT_OK,intent);
        finish();
      }
    });


  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setResult(101);
  }
}

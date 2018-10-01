package com.fitkits.Misc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fitkits.R;

public class LinkSocialMediaActivity extends AppCompatActivity {
    Button cancel;
    LinearLayout facebook_connection, twitter_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_link_social);
        cancel = (Button) findViewById(R.id.close);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        facebook_connection = findViewById(R.id.Facebook_connect);
        facebook_connection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, R.string.TXT_SOCIAL_CAPTION);
                intent.setType("text/plain");
                startActivity(intent);

            }
        });
        twitter_connect=findViewById(R.id.twitter_connection);
        twitter_connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, R.string.TXT_SOCIAL_CAPTION);
                intent.setType("text/plain");
                startActivity(intent);

            }
        });
    }


//    public void openSharex(View view) {
//        Intent intent=new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT,R.string.TXT_SOCIAL_CAPTION);
//        intent.setType("text/plain");
//        startActivity(intent);
//    }
}

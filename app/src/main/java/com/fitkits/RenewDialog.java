package com.fitkits;

import static com.fitkits.LoginFragment.loginFragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;

/**
 * Created by akshay on 10/07/17.
 */
public class RenewDialog extends BlurDialogFragment {


    Button renew;
TextView skip,remainDays;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.renew);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        renew=(Button)dialog.findViewById(R.id.renew);
        skip=(TextView) dialog.findViewById(R.id.skip);
        remainDays=(TextView) dialog.findViewById(R.id.daysRemain);
        if(getArguments()!=null){
            remainDays.setText(String.valueOf(getArguments().getLong("daysRemain")));
        }


        renew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Intent intent=new Intent(getActivity(), SubscriptionActivity.class);
                if(getArguments()!=null&&getArguments().getString("start").equals("1"))
                intent.putExtra("start","1");
                startActivity(intent);
                //getActivity().finish();
            }
        });
        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getArguments()!=null&&getArguments().getLong("daysRemain")>0) {
                    startActivity(new Intent(getActivity(), GoalActivity.class));
                    getActivity().finish();
                }
                else{
                    Toast.makeText(getActivity(),"Membership due, please continue",Toast.LENGTH_SHORT).show();
                }


            }
        });


        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //       if(googleApiClient!=null)
    }

    @NonNull
    @Override
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder()
                .overlayColor(Color.argb(136, 0, 0, 0))  // semi-transparent white color
                .debug(true)
                .build();
    }
    public interface InterfaceCommunicatorOtp {
        void  sendRequestCodeOtp();
    }
    @Override
    public void onStop() {
        super.onStop();
    }








}



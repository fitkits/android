package com.fitkits;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurDialogFragment;

import org.w3c.dom.Text;

/**
 * Created by akshay on 10/07/17.
 */
public class SubscribedDialog extends BlurDialogFragment {


    Button done;
    ImageView sub_icon;
    TextView price, duration, desc;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.subscribed_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        done = (Button) dialog.findViewById(R.id.done);
        price = (TextView) dialog.findViewById(R.id.price);
        duration = (TextView) dialog.findViewById(R.id.duration);
        sub_icon = (ImageView) dialog.findViewById(R.id.sub_icon);

        desc = (TextView) dialog.findViewById(R.id.desc);
        if (getArguments() != null) {
            float amount = ((Float.parseFloat(getArguments().getString("price"))) / 100);

            price.setText(String.valueOf(amount));
            if ((Double.parseDouble(getArguments().getString("validity")) / 30) >= 12) {
                int year = (int) (Double.parseDouble(getArguments().getString("validity")) / 30) / 12;
                if (year == 1) {
                    duration.setText(String.valueOf(year) + " Year");

                } else {
                    duration.setText(String.valueOf(year) + " Years");

                }

            } else {
                int months = (int) (Double.parseDouble(getArguments().getString("validity")) / 30);
                if (months == 1) {
                    duration.setText(String.valueOf(months) + " Month");
                } else {
                    duration.setText(String.valueOf(months) + " Months");

                }

            }
            // sub_icon.setImageDrawable(getResources().getDrawable(R.drawable.strong));
            desc.setText("Your subscription for " + getArguments().getString("title") + " plan has been activated.");

        }

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments() != null && getArguments().getString("start").equals("1")) {
                    dismiss();
                    startActivity(new Intent(getActivity(), GoalActivity.class));
                    getActivity().finish();
                } else {
                    getActivity().finish();
                    dismiss();
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
        void sendRequestCodeOtp();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}



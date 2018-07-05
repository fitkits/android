package com.fitkits;

import static com.fitkits.OnboardActivity.back;
import static com.fitkits.OnboardActivity.disable_overlay;
import static com.fitkits.OnboardActivity.done;
import static com.fitkits.OnboardActivity.next;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class fragment_onboard1 extends Fragment {
CardView mobile_login;
  RadioButton rdbtnMale,rdbtnFemale;
  public static Boolean flagmale=false,flagfemale=false;
public static fragment_onboard1 loginFragment;
  public fragment_onboard1() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.onboard1, container, false);
    rdbtnMale=(RadioButton) view.findViewById(R.id.male);
    rdbtnFemale=(RadioButton) view.findViewById(R.id.female);
    rdbtnMale.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        disable_overlay.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        done.setEnabled(true);
        if (rdbtnMale.isChecked()) {
          if (!flagmale) {
            rdbtnMale.setChecked(true);
            rdbtnFemale.setChecked(false);
            flagmale = true;
            flagfemale = false;
          } else {
            flagmale = false;
            rdbtnMale.setChecked(false);
            rdbtnFemale.setChecked(false);
          }
        }
        if (!rdbtnMale.isChecked()) {
          rdbtnMale.setChecked(true);
          rdbtnFemale.setChecked(false);
        }
      }
    });


    rdbtnFemale.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        disable_overlay.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        done.setEnabled(true);
        if (rdbtnFemale.isChecked()) {
          if (!flagfemale) {
            rdbtnFemale.setChecked(true);
            rdbtnMale.setChecked(false);
            flagfemale = true;
            flagmale = false;
          } else {
            flagfemale = false;
            rdbtnFemale.setChecked(false);
            rdbtnMale.setChecked(false);
          }
        }
        if (rdbtnFemale.isChecked()) {
          rdbtnFemale.setChecked(true);
          rdbtnMale.setChecked(false);
        }
        }
    });


    return view;

  }

}

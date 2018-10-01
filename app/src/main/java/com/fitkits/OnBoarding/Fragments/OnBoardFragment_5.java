package com.fitkits.OnBoarding.Fragments;

import static com.fitkits.OnBoarding.Activities.OnboardActivity.back;
import static com.fitkits.OnBoarding.Activities.OnboardActivity.disable_overlay;
import static com.fitkits.OnBoarding.Activities.OnboardActivity.done;
import static com.fitkits.OnBoarding.Activities.OnboardActivity.next;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.fitkits.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OnBoardFragment_5 extends Fragment {
CardView mobile_login;
  RadioButton rdbtnVeg,rdbtnNonVeg;
  public static Boolean flagVeg=false,flagNonVeg=false;
public static OnBoardFragment_5 loginFragment;
  public OnBoardFragment_5() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.onboard5, container, false);
    rdbtnVeg=(RadioButton) view.findViewById(R.id.veg);
    rdbtnNonVeg=(RadioButton) view.findViewById(R.id.nonveg);
    rdbtnVeg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        disable_overlay.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        done.setEnabled(true);
        if (rdbtnVeg.isChecked()) {
          if (!flagVeg) {
            rdbtnVeg.setChecked(true);
            rdbtnNonVeg.setChecked(false);
            flagVeg = true;
            flagNonVeg = false;
          } else {
            flagVeg = false;
            rdbtnVeg.setChecked(false);
            rdbtnNonVeg.setChecked(false);
          }
        }
        if (!rdbtnVeg.isChecked()) {
          rdbtnVeg.setChecked(true);
          rdbtnNonVeg.setChecked(false);
        }
      }
    });


    rdbtnNonVeg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        disable_overlay.setVisibility(View.GONE);
        next.setEnabled(true);
        back.setEnabled(true);
        done.setEnabled(true);
        if (rdbtnNonVeg.isChecked()) {
          if (!flagNonVeg) {
            rdbtnNonVeg.setChecked(true);
            rdbtnVeg.setChecked(false);
            flagNonVeg = true;
            flagVeg = false;
          } else {
            flagNonVeg = false;
            rdbtnNonVeg.setChecked(false);
            rdbtnVeg.setChecked(false);
          }
        }
         if(!rdbtnNonVeg.isChecked()) {

           rdbtnNonVeg.setChecked(true);
           rdbtnVeg.setChecked(false);        }
      }
    });
    return view;
  }

}
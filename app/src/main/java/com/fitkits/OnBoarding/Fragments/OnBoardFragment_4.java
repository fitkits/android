package com.fitkits.OnBoarding.Fragments;

import static com.fitkits.OnBoarding.Activities.OnboardActivity.back;
import static com.fitkits.OnBoarding.Activities.OnboardActivity.disable_overlay;
import static com.fitkits.OnBoarding.Activities.OnboardActivity.done;
import static com.fitkits.OnBoarding.Activities.OnboardActivity.next;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fitkits.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OnBoardFragment_4 extends Fragment {
CardView mobile_login;
public static EditText weight;
public static OnBoardFragment_4 loginFragment;
  public OnBoardFragment_4() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.onboard4, container, false);
    weight=(EditText)view.findViewById(R.id.weight);
    weight.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        if(weight.getText().length()>0){
          disable_overlay.setVisibility(View.GONE);
          next.setEnabled(true);
          back.setEnabled(true);
          done.setEnabled(true);
        }
        else {
          disable_overlay.setVisibility(View.VISIBLE);
          next.setEnabled(false);
          back.setEnabled(false);
          done.setEnabled(false);
        }

      }
    });
    return view;
  }

}

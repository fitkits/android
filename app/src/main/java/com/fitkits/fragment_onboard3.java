package com.fitkits;

import static com.fitkits.OnboardActivity.back;
import static com.fitkits.OnboardActivity.disable_overlay;
import static com.fitkits.OnboardActivity.done;
import static com.fitkits.OnboardActivity.next;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class fragment_onboard3 extends Fragment {
CardView mobile_login;
public static fragment_onboard3 loginFragment;
public static EditText feet,inches;
  public fragment_onboard3() {
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.onboard3, container, false);
    feet=(EditText)view.findViewById(R.id.feet);
    inches=(EditText)view.findViewById(R.id.inches);
    feet.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        if((inches.getText().length()>0)||feet.getText().length()>0){
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
    inches.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        if(Integer.parseInt(editable.toString())>11){
          inches.setText("11");
        }
        if((inches.getText().length()>0)||feet.getText().length()>0){
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

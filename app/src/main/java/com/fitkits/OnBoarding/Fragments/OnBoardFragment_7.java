package com.fitkits.OnBoarding.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fitkits.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OnBoardFragment_7 extends Fragment {
CardView mobile_login;
public static OnBoardFragment_7 loginFragment;
  public OnBoardFragment_7() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.onboard7, container, false);
    return view;
  }

}

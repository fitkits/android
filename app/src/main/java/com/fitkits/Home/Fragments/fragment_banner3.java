package com.fitkits.Home.Fragments;

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
public class fragment_banner3 extends Fragment {
CardView mobile_login;
public static fragment_banner3 loginFragment;
  public fragment_banner3() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.banner3, container, false);
    return view;
  }

}

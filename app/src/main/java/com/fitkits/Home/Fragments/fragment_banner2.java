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
public class fragment_banner2 extends Fragment {
CardView mobile_login;
public static fragment_banner2 loginFragment;
  public fragment_banner2() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.banner2, container, false);
    return view;
  }

}

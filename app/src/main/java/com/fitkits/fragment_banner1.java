package com.fitkits;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class fragment_banner1 extends Fragment {
CardView mobile_login;
public static fragment_banner1 loginFragment;
  public fragment_banner1() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.banner1, container, false);
    return view;
  }

}

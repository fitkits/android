package com.fitkits.Misc.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fitkits.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashScreenFragment extends Fragment {

  public SplashScreenFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_splash, container, false);
  }
}

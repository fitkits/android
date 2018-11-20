package com.fitkits.Misc.Fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fitkits.R;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashScreenFragment extends Fragment {

  TextView versionText;
  String version;
  Integer versionCode;
  public SplashScreenFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_splash, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    versionText = (TextView) getView().findViewById(R.id.versionText);

    try {
      PackageInfo pInfo = getActivity().getApplicationContext().getPackageManager()
              .getPackageInfo(getActivity().getApplicationContext().getPackageName(), 0);
      version = pInfo.versionName;
      versionCode = (pInfo.versionCode);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    versionText.setText("v. " + version + " build ftks_b" + versionCode.toString());
  }
}

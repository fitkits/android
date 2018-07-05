package com.fitkits;

import static com.fitkits.OnboardActivity.back;
import static com.fitkits.OnboardActivity.disable_overlay;
import static com.fitkits.OnboardActivity.done;
import static com.fitkits.OnboardActivity.next;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.fitkits.fragment_onboard2.SpinnerAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class fragment_onboard6 extends Fragment {
CardView mobile_login;

  public  static Spinner blood_group;
  public static List<String> bloodGroupList=new ArrayList<String>();
public static fragment_onboard6 loginFragment;
  public fragment_onboard6() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view= inflater.inflate(R.layout.onboard6, container, false);
    blood_group=(Spinner)view.findViewById(R.id.bloodGroup);
    int currentyear= Calendar.getInstance().get(Calendar.YEAR);
    bloodGroupList.add("Click to choose");
    bloodGroupList.add("A+");
    bloodGroupList.add("A-");
    bloodGroupList.add("B+");
    bloodGroupList.add("B-");
    bloodGroupList.add("O+");
    bloodGroupList.add("O-");
    bloodGroupList.add("AB+");
    bloodGroupList.add("AB-");

    blood_group.setAdapter(new SpinnerAdapter(getContext(),R.layout.year_item,bloodGroupList));

    blood_group.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i!=0){
          disable_overlay.setVisibility(View.GONE);
          next.setEnabled(true);
          back.setEnabled(true);
          done.setEnabled(true);
        }
        else{
          disable_overlay.setVisibility(View.VISIBLE);
          next.setEnabled(false);
          back.setEnabled(false);
          done.setEnabled(false);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    return view;
  }
  public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);

      // TODO Auto-generated constructor stub
    }

    @Override
    public View getDropDownView(int position, View convertView,
        ViewGroup parent) {
      // TODO Auto-generated method stub
      return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      //return super.getView(position, convertView, parent);

      LayoutInflater inflater=getActivity().getLayoutInflater();
      View row=inflater.inflate(R.layout.year_item, parent, false);
      TextView role=(TextView)row.findViewById(R.id.year);
      if(position==0)
        role.setTextColor(getResources().getColor(R.color.black15));
      else
        role.setTextColor(getResources().getColor(R.color.black));

      role.setText(bloodGroupList.get(position));

      return row;
    }
  }
}

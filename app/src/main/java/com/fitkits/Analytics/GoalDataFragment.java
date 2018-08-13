package com.fitkits.Analytics;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.fitkits.ApiService;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.WeeklyData;
import com.fitkits.R;
import com.fitkits.RetroClient;
import com.fitkits.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GoalDataFragment extends Fragment {

    SharedPreferences myPrefs;
    LayoutManager layoutManager;
    DividerItemDecoration mDividerItemDecoration;
    RecyclerView weightDataRecycleView;
    List<List<String>>  datesList=new ArrayList<List<String>>();
    Bundle bn;
    String type;
    ProgressDialog progressDialog;
    int position=0;
    Realm realmUI;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_weight_data, container, false);
        weightDataRecycleView=(RecyclerView)layout.findViewById(R.id.weightDataRecycleView);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        weightDataRecycleView.setLayoutManager(layoutManager);
        weightDataRecycleView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        realmUI=Realm.getDefaultInstance();
        myPrefs= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        datesList.clear();
        bn=getArguments();
        if(bn!=null){
            type=bn.getString("type");
            position=bn.getInt("position");
            Gson gson = new Gson();
            Type type = new TypeToken<List<List<String>>>() {
            }.getType();
            String dates = bn.getString("dates");
            datesList = gson.fromJson(dates, type);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

            try {
                if(position<datesList.size()) {
                    Date startD = format.parse(datesList.get(position).get(0));
                    Date endD = format.parse(datesList.get(position).get(1));
                    Calendar calStart = Calendar.getInstance();
                    Calendar calEnd = Calendar.getInstance();
                    calStart.setTime(startD);
                    calEnd.setTime(endD);

                    calEnd.set(Calendar.HOUR_OF_DAY, 23);
                    calEnd.set(Calendar.MINUTE, 59);
                    calEnd.set(Calendar.SECOND, 00);
                    calEnd.set(Calendar.MILLISECOND, 000);
                    getWeightAggregate(calStart.getTime(), calEnd.getTime());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        /*
        dataList.add(new WeightData("Today","45"));
        dataList.add(new WeightData("05/03","36"));
        dataList.add(new WeightData("04/03","49"));

        weightDataRecycleView.setAdapter(new GoalDataAdapter(getActivity(),dataList));*/



        return  layout;
    }

    void getWeightAggregate(Date stDate,Date enDate) {

        ApiService apiService= RetroClient
            .getApiService(myPrefs.getString("token", ""),getActivity().getApplicationContext());

        SimpleDateFormat formatNew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatNew.setTimeZone(TimeZone.getTimeZone("UTC"));

            apiService.getWeeklyAggregate("/api/v1/cms/answers?start="+formatNew.format(stDate)+"&end="+formatNew.format(enDate)+"&user="+myPrefs.getString("user_id","")).subscribeOn(
                Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<ItemParent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Loading....");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                    }

                    @Override
                    public void onNext(ItemParent itemParent) {
                        if (progressDialog.isShowing() && this != null) {
                            progressDialog.dismiss();
                        }

                        List<WeeklyData> value=itemParent.getAnswers();

                        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd'T'hh:mm:ss.SSS'Z'");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                        SimpleDateFormat sdf_new = new SimpleDateFormat("MMM dd, yyyy");
                        SimpleDateFormat sdf_new_rev = new SimpleDateFormat("yyyy-MM-dd");

                        SimpleDateFormat sdf_graph = new SimpleDateFormat("dd/MM");

                        Date startD=new Date();
                        Date endD=new Date();
                        String startDate=sdf_new_rev.format(stDate);
                        String endDate=sdf_new_rev.format(enDate);
                        try {
                            startD = sdf_new_rev.parse(startDate);
                            endD = sdf_new_rev.parse(endDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendarStart=Calendar.getInstance();
                        Calendar calendarEnd=Calendar.getInstance();
                        calendarStart.setTime(startD);
                        calendarEnd.setTime(endD);
                        ArrayList<GoalData> weekdatesList=new ArrayList<GoalData>();
                        if(calendarStart.get(Calendar.DATE)==calendarEnd.get(Calendar.DATE)){
                            weekdatesList.add(new GoalData(sdf_graph.format(calendarEnd.getTime()), 0));
                        }
                        else {
                            while (calendarStart.get(Calendar.DATE) != calendarEnd.get(Calendar.DATE)) {
                                Date curDate = calendarEnd.getTime();
                                weekdatesList.add(new GoalData(sdf_graph.format(curDate), 0));
                                calendarEnd.add(Calendar.DATE, -1);
                            }
                            weekdatesList.add(new GoalData(sdf_graph.format(calendarEnd.getTime()), 0));

                        }
                        float sumval=0;

                        for(int i=0;i<value.size();i++){
                            if (value.get(i).getType().equals(type)) {

                                for(int j=0;j<value.size();j++) {
                                    if (value.get(j).getType().equals(type)) {

                                        try {
                                            if (sdf_graph.format(sdf.parse(value.get(i).getTimeStamp()))
                                                .equals(sdf_graph.format(sdf.parse(value.get(j).getTimeStamp())))){
                                                sumval = sumval + value.get(j).getValue();
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                try {
                                    Date date = sdf.parse(value.get(i).getTimeStamp());
                                    String dateGraphString = sdf_graph.format(date);

                                    for (int j = 0; j < weekdatesList.size(); j++) {
                                        if (dateGraphString
                                            .equalsIgnoreCase(weekdatesList.get(j).getDate())) {
                                            weekdatesList.get(j).setVal(
                                                sumval);
                                            sumval=0;

                                        }
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        weightDataRecycleView.setAdapter(new GoalDataAdapter(getContext(),weekdatesList,position,type));





                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog.isShowing() && this != null) {
                            progressDialog.dismiss();
                        }
                        Log.d("Response", e.getMessage());
                        Toast.makeText(getContext(),
                            "Something went wrong. Please try again later.",
                            Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
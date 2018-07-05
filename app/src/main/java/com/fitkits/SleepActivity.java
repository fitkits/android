package com.fitkits;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class SleepActivity extends AppCompatActivity {
  protected BarChart mChart;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sleep);
    mChart=(BarChart)findViewById(R.id.bchart);
    mChart.setDrawBarShadow(false);
    mChart.setDrawValueAboveBar(true);

    mChart.getDescription().setEnabled(false);

    // if more than 60 entries are displayed in the chart, no values will be
    // drawn
    mChart.setMaxVisibleValueCount(60);

    // scaling can now only be done on x- and y-axis separately
    mChart.setPinchZoom(false);

    mChart.setDrawGridBackground(false);
    // mChart.setDrawYLabels(false);


    XAxis xAxis = mChart.getXAxis();
    xAxis.setPosition(XAxisPosition.BOTTOM);
    xAxis.setDrawGridLines(false);
    xAxis.setGranularity(1f); // only intervals of 1 day
    xAxis.setLabelCount(7);


    YAxis leftAxis = mChart.getAxisLeft();
    leftAxis.setLabelCount(8, false);
    leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
    leftAxis.setSpaceTop(15f);
    leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

    YAxis rightAxis = mChart.getAxisRight();
    rightAxis.setDrawGridLines(false);
    rightAxis.setLabelCount(8, false);
    rightAxis.setSpaceTop(15f);
    rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

    Legend l = mChart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    l.setDrawInside(false);
    l.setForm(LegendForm.SQUARE);
    l.setFormSize(9f);
    l.setTextSize(11f);
    l.setXEntrySpace(4f);
    // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
    // "def", "ghj", "ikl", "mno" });
    // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
    // "def", "ghj", "ikl", "mno" });


    setData(7);

  }
  private void setData(int count) {

    float start = 1f;

    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

    for (int i = 0; i < count; i++) {


        yVals1.add(new BarEntry(i, 10));
    }

    BarDataSet set1;

    if (mChart.getData() != null &&
        mChart.getData().getDataSetCount() > 0) {
      set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
      set1.setValues(yVals1);
      mChart.getData().notifyDataChanged();
      mChart.notifyDataSetChanged();
    } else {
      set1 = new BarDataSet(yVals1, "The year 2017");

      set1.setDrawIcons(false);

      set1.setColor(Color.WHITE);

      ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
      dataSets.add(set1);

      BarData data = new BarData(dataSets);
      data.setValueTextSize(10f);
      data.setBarWidth(0.7f);

      mChart.setData(data);
    }
  }


}

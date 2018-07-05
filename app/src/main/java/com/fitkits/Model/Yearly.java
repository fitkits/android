package com.fitkits.Model;

import java.math.BigDecimal;

/**
 * Created by akshay on 22/03/18.
 */

public class Yearly {

  String quarter;
  float average;

  public Yearly(String quarter,float average){
    this.quarter=quarter;
    this.average=average;
  }

  public String getQuarter() {
    return quarter;
  }

  public void setQuarter(String quarter) {
    this.quarter = quarter;
  }

  public float getAverage() {
    return average;
  }

  public void setAverage(float average) {
    BigDecimal bd = new BigDecimal(Float.toString(average));
    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    this.average = bd.floatValue();
  }
}

package com.fitkits.Model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by akshay on 22/03/18.
 */

public class Quarterly {

  String month;
  float average;

  public Quarterly(String month,float average){
    this.month=month;
    this.average=average;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public float getAverage() {
    return average;
  }

  public void setAverage(float average) {
    BigDecimal bd = new BigDecimal(Float.toString(average));
    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    this.average = bd.floatValue();  }
}

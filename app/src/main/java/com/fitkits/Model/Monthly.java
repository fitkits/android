package com.fitkits.Model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by akshay on 22/03/18.
 */

public class Monthly {

  List<String> dates;
  float average;

  public Monthly(List<String> dates,float average){
    this.dates=dates;
    this.average=average;
  }

  public Monthly(float average){
    this.average=average;
  }
  public List<String> getDates() {
    return dates;
  }

  public void setDates(List<String> dates) {
    this.dates = dates;
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

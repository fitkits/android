package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Value {

  @SerializedName("average")
  @Expose
  private Double average;
  @SerializedName("averageType")
  @Expose
  private String averageType;
  @SerializedName("week")
  @Expose
  private Integer week;
  @SerializedName("month")
  @Expose
  private Integer month;
  @SerializedName("year")
  @Expose
  private Integer year;

  @SerializedName("quarter")
  @Expose
  private String quarter;

  @SerializedName("user")
  @Expose
  private String user;
  @SerializedName("status")
  @Expose
  private String status;
  @SerializedName("percentage")
  @Expose
  private float percentage;


  public String getQuarter() {
    return quarter;
  }

  public void setQuarter(String quarter) {
    this.quarter = quarter;
  }

  public Double getAverage() {
    BigDecimal bd = new BigDecimal(Double.toString(average));
    bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    return Double.parseDouble(decimalFormat.format(average));

  }

  public void setAverage(Double average) {

    this.average = average;
  }

  public String getAverageType() {
    return averageType;
  }

  public void setAverageType(String averageType) {
    this.averageType = averageType;
  }

  public Integer getWeek() {
    return week;
  }

  public void setWeek(Integer week) {
    this.week = week;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public float getPercentage() {
    return percentage;
  }

  public void setPercentage(float percentage) {
    this.percentage = percentage;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("average", average).append("averageType", averageType)
        .append("week", week).append("month", month).append("year", year).toString();
  }

}

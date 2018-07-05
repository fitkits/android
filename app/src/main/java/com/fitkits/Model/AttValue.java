package com.fitkits.Model;

import com.fitkits.Model.Average;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AttValue {

  @SerializedName("average")
  @Expose
  private List<Average> average = null;

  public List<Average> getAverage() {
    return average;
  }

  public void setAverage(List<Average> average) {
    this.average = average;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("average", average).toString();
  }
}
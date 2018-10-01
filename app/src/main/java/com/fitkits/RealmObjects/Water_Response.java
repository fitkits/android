package com.fitkits.RealmObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Water_Response extends RealmObject {
  @SerializedName("goal")
  @Expose
  private Water_Goal_ goal;
  @SerializedName("data")
  @Expose
  private Water_Data data;

  Water_Response(Water_Goal_ goal,Water_Data data){
    this.goal=goal;
    this.data=data;
  }
  public Water_Response(){

  }

  public Water_Goal_ getGoal() {
    return goal;
  }

  public void setGoal(Water_Goal_ goal) {
    this.goal = goal;
  }

  public Water_Data getData() {
    return data;
  }

  public void setData(Water_Data data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("goal", goal).append("data", data).toString();
  }





}

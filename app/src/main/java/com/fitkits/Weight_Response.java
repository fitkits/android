package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Weight_Response extends RealmObject {
  @SerializedName("goal")
  @Expose
  private Weight_Goal_ goal;
  @SerializedName("data")
  @Expose
  private Weight_Data data;

  Weight_Response(Weight_Goal_ goal,Weight_Data data){
    this.goal=goal;
    this.data=data;
  }
  public Weight_Response(){

  }

  public Weight_Goal_ getGoal() {
    return goal;
  }

  public void setGoal(Weight_Goal_ goal) {
    this.goal = goal;
  }

  public Weight_Data getData() {
    return data;
  }

  public void setData(Weight_Data data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("goal", goal).append("data", data).toString();
  }





}

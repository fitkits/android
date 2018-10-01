package com.fitkits.RealmObjects;

import com.fitkits.Goals.Beans.Goal_;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Response extends RealmObject {
@SerializedName("goal")
@Expose
private Goal_ goal;
@SerializedName("data")
@Expose
private Data data;

Response(Goal_ goal,Data data){
  this.goal=goal;
  this.data=data;
}
public Response(){

}

public Goal_ getGoal() {
return goal;
}

public void setGoal(Goal_ goal) {
this.goal = goal;
}

public Data getData() {
return data;
}

public void setData(Data data) {
this.data = data;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("goal", goal).append("data", data).toString();
}






}

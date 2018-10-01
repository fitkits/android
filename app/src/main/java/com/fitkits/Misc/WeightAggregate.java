package com.fitkits.Misc;

import com.fitkits.RealmObjects.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WeightAggregate {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("user")
@Expose
private String user;
@SerializedName("responses")
@Expose
private Responses responses;
@SerializedName("modifiedAt")
@Expose
private String modifiedAt;
@SerializedName("__v")
@Expose
private Integer v;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getUser() {
return user;
}

public void setUser(String user) {
this.user = user;
}

public Responses getResponses() {
return responses;
}

public void setResponses(Responses responses) {
this.responses = responses;
}

public String getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(String modifiedAt) {
this.modifiedAt = modifiedAt;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("id", id).append("user", user).append("responses", responses).append("modifiedAt", modifiedAt).append("v", v).toString();
}

}
class Responses {

  @SerializedName("data")
  @Expose
  private Data data;
  @SerializedName("goal")
  @Expose
  private String goal;
  @SerializedName("_id")
  @Expose
  private String id;
  @SerializedName("timeStamp")
  @Expose
  private String timeStamp;

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public String getGoal() {
    return goal;
  }

  public void setGoal(String goal) {
    this.goal = goal;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("data", data).append("goal", goal).append("id", id).append("timeStamp", timeStamp).toString();
  }

}
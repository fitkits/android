
package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WaterLog extends RealmObject {

  @PrimaryKey
  private String date;

  int serverLog=0;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @SerializedName("user")
  @Expose
  private User_ user;
  @SerializedName("responses")
  @Expose
  private RealmList<Water_Response> responses = null;
  public WaterLog(){

  }


  public WaterLog(User_ user,RealmList<Water_Response> responses,String date,int serverLog){
    this.user=user;
    this.responses=responses;
    this.serverLog=serverLog;
    this.date=date;
  }

  public int getServerLog() {
    return serverLog;
  }

  public void setServerLog(int serverLog) {
    this.serverLog = serverLog;
  }

  public User_ getUser() {
    return user;
  }

  public void setUser(User_ user) {
    this.user = user;
  }

  public RealmList<Water_Response> getResponses() {
    return responses;
  }

  public void setResponses(RealmList<Water_Response> responses) {
    this.responses = responses;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this).append("user", user).append("responses", responses).toString();
  }

}





package com.fitkits.RealmObjects;

import com.fitkits.RealmObjects.User_;
import com.fitkits.RealmObjects.Weight_Response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class WeightLog extends RealmObject {

  @PrimaryKey
  private String date;

  public int serverLog=0;

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
  private RealmList<Weight_Response> responses = null;
  public WeightLog(){

  }

  WeightLog(User_ user,RealmList<Weight_Response> responses,String date){
    this.user=user;
    this.responses=responses;
    this.date=date;
  }
  WeightLog(User_ user,RealmList<Weight_Response> responses,String date,int serveLog){
    this.user=user;
    this.responses=responses;
    this.date=date;
    this.serverLog=serveLog;
  }

  public User_ getUser() {
    return user;
  }

  public void setUser(User_ user) {
    this.user = user;
  }

  public RealmList<Weight_Response> getResponses() {
    return responses;
  }

  public void setResponses(RealmList<Weight_Response> responses) {
    this.responses = responses;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this).append("user", user).append("responses", responses).toString();
  }

  public int getServerLog() {
    return serverLog;
  }

  public void setServerLog(int serverLog) {
    this.serverLog = serverLog;
  }

}





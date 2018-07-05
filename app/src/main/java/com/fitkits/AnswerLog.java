
package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AnswerLog extends RealmObject {

  @PrimaryKey
  private String date;

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
  private RealmList<Response> responses = null;
  public AnswerLog(){

  }

  AnswerLog(User_ user,RealmList<Response> responses){
    this.user=user;
    this.responses=responses;
  }

  public User_ getUser() {
    return user;
  }

  public void setUser(User_ user) {
    this.user = user;
  }

  public RealmList<Response> getResponses() {
    return responses;
  }

  public void setResponses(RealmList<Response> responses) {
    this.responses = responses;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this).append("user", user).append("responses", responses).toString();
  }

}




package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;

public class User_ extends RealmObject {


  @SerializedName("_id")
  @Expose
  private String id;

  public User_(String id) {
    this.id=id;

  }
  public User_(){

  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

package com.fitkits.Goals.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Goal_ extends RealmObject {


  @SerializedName("_id")
  @Expose
  private String id;

  Goal_(String id){
    this.id=id;
  }

  public Goal_(){

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }



  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).toString();
  }

}

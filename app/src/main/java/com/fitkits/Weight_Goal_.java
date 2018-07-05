package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Weight_Goal_ extends RealmObject {


  @SerializedName("_id")
  @Expose
  private String id;

  Weight_Goal_(String id) {
    this.id = id;
  }

  public Weight_Goal_() {

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

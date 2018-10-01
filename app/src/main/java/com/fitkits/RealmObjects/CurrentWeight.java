package com.fitkits.RealmObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CurrentWeight extends RealmObject {

 @SerializedName("value")
 @Expose
 private Integer value;

 CurrentWeight(int value){
   this.value=value;
 }
  public CurrentWeight(){

  }
 public Integer getValue() {
   return value;
 }

 public void setValue(Integer value) {
   this.value = value;
 }

 @Override
 public String toString() {
   return new ToStringBuilder(this).append("value", value).toString();
 }

}

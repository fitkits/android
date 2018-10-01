package com.fitkits.RealmObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DataField extends RealmObject {

 @SerializedName("field")
 @Expose
 private String field;
 @SerializedName("unit")
 @Expose
 private String unit;
 @PrimaryKey
 @SerializedName("_id")
 @Expose
 private String id;

 private String value="";

 public DataField(){

 }

 public String getField() {
   return field;
 }

 public void setField(String field) {
   this.field = field;
 }

 public String getUnit() {
   return unit;
 }

 public void setUnit(String unit) {
   this.unit = unit;
 }

 public String getId() {
   return id;
 }

 public void setId(String id) {
   this.id = id;
 }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}

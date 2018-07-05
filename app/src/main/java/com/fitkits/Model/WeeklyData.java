package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WeeklyData {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("user")
@Expose
private String user;
@SerializedName("type")
@Expose
private String type;
@SerializedName("value")
@Expose
private float value;
@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("timestamp")
@Expose
private String timeStamp;
@SerializedName("modifiedAt")
@Expose
private String modifiedAt;

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

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public float getValue() {
return value;
}

public void setValue(float value) {
  BigDecimal bd = new BigDecimal(Float.toString(value));
  bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
  this.value = bd.floatValue();
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

public String getTimeStamp() throws ParseException {



  return timeStamp;
}

public void setTimeStamp(String timeStamp) {
this.timeStamp = timeStamp;
}

public String getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(String modifiedAt) {
this.modifiedAt = modifiedAt;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("id", id).append("user", user).append("type", type).append("value", value).append("v", v).append("timeStamp", timeStamp).append("modifiedAt", modifiedAt).toString();
}

}
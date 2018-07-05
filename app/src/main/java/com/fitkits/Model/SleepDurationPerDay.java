package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SleepDurationPerDay extends RealmObject {

@SerializedName("units")
@Expose
private String units;
@SerializedName("value")
@Expose
private Integer value;
@SerializedName("wakeUpTime")
@Expose
private String wakeUpTime;
@SerializedName("bedTime")
@Expose
private String bedTime;
  public SleepDurationPerDay(){
  }
  public SleepDurationPerDay(String bedTime,String wakeUpTime,int value){
    this.wakeUpTime=wakeUpTime;
    this.bedTime=bedTime;
    this.value=value;
  }

public String getUnits() {
return units;
}

public void setUnits(String units) {
this.units = units;
}

public Integer getValue() {
return value;
}

public void setValue(Integer value) {
this.value = value;
}

public String getWakeUpTime() {
return wakeUpTime;
}

public void setWakeUpTime(String wakeUpTime) {
this.wakeUpTime = wakeUpTime;
}

public String getBedTime() {
return bedTime;
}

public void setBedTime(String bedTime) {
this.bedTime = bedTime;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("units", units).append("value", value).append("wakeUpTime", wakeUpTime).append("bedTime", bedTime).toString();
}

}

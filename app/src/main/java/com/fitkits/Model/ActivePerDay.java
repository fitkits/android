package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ActivePerDay extends RealmObject {

@SerializedName("units")
@Expose
private String units;
@SerializedName("value")
@Expose
private Integer value;

  public ActivePerDay(Integer value){
    this.value=value;
  }
  public ActivePerDay(){
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

@Override
public String toString() {
return new ToStringBuilder(this).append("units", units).append("value", value).toString();
}

}

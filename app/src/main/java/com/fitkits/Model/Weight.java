package com.fitkits.Model;

import android.widget.ImageView;
import com.fitkits.WeightDialog.InterfaceCommunicator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Weight extends RealmObject {

@SerializedName("units")
@Expose
private String units;
@SerializedName("value")
@Expose
private Integer value;
@SerializedName("goalEndTime")
@Expose
String weightGoalDate;

public Weight(Integer value,String weightGoalDate){
  this.value=value;
  this.weightGoalDate=weightGoalDate;
}

  public Weight(){
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

  public String getWeightGoalDate() {
    return weightGoalDate;
  }

  public void setWeightGoalDate(String weightGoalDate) {
    this.weightGoalDate = weightGoalDate;
  }

  @Override
public String toString() {
return new ToStringBuilder(this).append("units", units).append("value", value).toString();
}

}

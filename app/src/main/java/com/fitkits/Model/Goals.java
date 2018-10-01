package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Goals extends RealmObject {

@SerializedName("weight")
@Expose
private Weight weight;
@SerializedName("waterConsumptionPerDay")
@Expose
private WaterConsumptionPerDay waterConsumptionPerDay;
@SerializedName("sleepDurationPerDay")
@Expose
private SleepDurationPerDay sleepDurationPerDay;
@SerializedName("caloriesPerDay")
@Expose
private CaloriesPerDay caloriesPerDay;
@SerializedName("activePerDay")
@Expose
private ActivePerDay activePerDay;
  public Goals(){
  }
  public Goals(WaterConsumptionPerDay waterConsumptionPerDay){
  this.waterConsumptionPerDay=waterConsumptionPerDay;
}
  public Goals(SleepDurationPerDay sleepDurationPerDay){
    this.sleepDurationPerDay=sleepDurationPerDay;
  }
  public Goals(Weight weight){
    this.weight=weight;
  }
  public Goals(CaloriesPerDay caloriesPerDay){
    this.caloriesPerDay=caloriesPerDay;
  }
  public Goals(ActivePerDay activePerDay){
    this.activePerDay=activePerDay;
  }
public Weight getWeight() {
return weight;
}

public void setWeight(Weight weight) {
this.weight = weight;
}

public WaterConsumptionPerDay getWaterConsumptionPerDay() {
return waterConsumptionPerDay;
}

public void setWaterConsumptionPerDay(WaterConsumptionPerDay waterConsumptionPerDay) {
this.waterConsumptionPerDay = waterConsumptionPerDay;
}

public SleepDurationPerDay getSleepDurationPerDay() {
return sleepDurationPerDay;
}

public void setSleepDurationPerDay(SleepDurationPerDay sleepDurationPerDay) {
this.sleepDurationPerDay = sleepDurationPerDay;
}

public CaloriesPerDay getCaloriesPerDay() {
return caloriesPerDay;
}

public void setCaloriesPerDay(CaloriesPerDay caloriesPerDay) {
this.caloriesPerDay = caloriesPerDay;
}

public ActivePerDay getActivePerDay() {
return activePerDay;
}

public void setActivePerDay(ActivePerDay activePerDay) {
this.activePerDay = activePerDay;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("weight", weight).append("waterConsumptionPerDay", waterConsumptionPerDay).append("sleepDurationPerDay", sleepDurationPerDay).append("caloriesPerDay", caloriesPerDay).append("activePerDay", activePerDay).toString();
}

}

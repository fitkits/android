package com.fitkits.RealmObjects;

import com.fitkits.Goals.Beans.GoalDate;
import com.fitkits.Goals.Beans.GoalWeight;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Data extends RealmObject {

  @SerializedName("goalDate")
  @Expose
  private GoalDate goalDate;
  @SerializedName("goalWeight")
  @Expose
  private GoalWeight goalWeight;
  @SerializedName("recordedDate")
  @Expose
  private RecordedDate recordedDate;
  @SerializedName("currentWeight")
  @Expose
  private CurrentWeight currentWeight;
  @SerializedName("numberOfCups")
  @Expose
  private NumberOfCups numberOfCups;

  public Data(CurrentWeight currentWeight, RecordedDate recordedDate, GoalWeight goalWeight, GoalDate goalDate){
    this.currentWeight=currentWeight;
    this.recordedDate=recordedDate;

    this.goalWeight=goalWeight;
    this.goalDate=goalDate;

  }
  public Data(NumberOfCups numberOfCups){
    this.numberOfCups=numberOfCups;
  }
  public Data(){

  }

  public GoalDate getGoalDate() {
    return goalDate;
  }

  public void setGoalDate(GoalDate goalDate) {
    this.goalDate = goalDate;
  }

  public GoalWeight getGoalWeight() {
    return goalWeight;
  }

  public void setGoalWeight(GoalWeight goalWeight) {
    this.goalWeight = goalWeight;
  }

  public RecordedDate getRecordedon() {
    return recordedDate;
  }

  public void setRecordedon(RecordedDate recordedon) {
    this.recordedDate = recordedon;
  }

  public CurrentWeight getCurrentWeight() {
    return currentWeight;
  }

  public void setCurrentWeight(CurrentWeight currentWeight) {
    this.currentWeight = currentWeight;
  }

  public NumberOfCups getNumberOfCups() {
    return numberOfCups;
  }

  public void setNumberOfCups(NumberOfCups numberOfCups) {
    this.numberOfCups = numberOfCups;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("goalDate", goalDate).append("goalWeight", goalWeight).append("recordedon", recordedDate).append("currentWeight", currentWeight).append("numberOfCups", numberOfCups).toString();
  }

}

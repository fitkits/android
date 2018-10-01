package com.fitkits.Goals.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GoalDate extends RealmObject {

    @SerializedName("value")
    @Expose
    private String value;

     public GoalDate(String value){
       this.value=value;

     }
     public GoalDate(){

     }
    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("value", value).toString();
    }

  }

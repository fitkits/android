package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GoalWeight extends RealmObject {

    @SerializedName("value")
    @Expose
    private Integer value;

    public GoalWeight(int value){
      this.value=value;

    }
     public GoalWeight(){

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

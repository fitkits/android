package com.fitkits.RealmObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RecordedDate extends RealmObject {

    @SerializedName("value")
    @Expose
    private String value;

    RecordedDate(String value){
      this.value=value;
    }
     public RecordedDate(){

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

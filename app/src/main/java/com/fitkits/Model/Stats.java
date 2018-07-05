package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Stats extends RealmObject {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("value")
@Expose
private float value;
@SerializedName("timeStamp")
@Expose
private String timeStamp;

@SerializedName("image")
@Expose
private String image;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public float getValue() {
return value;
}

public void setValue(float value) {
this.value = value;
}

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getTimeStamp() {
return timeStamp;
}

public void setTimeStamp(String timeStamp) {
this.timeStamp = timeStamp;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("id", id).append("value", value).append("timeStamp", timeStamp).toString();
}

}

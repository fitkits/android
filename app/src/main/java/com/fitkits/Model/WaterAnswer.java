package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class WaterAnswer extends RealmObject {

  @PrimaryKey
  private String date;

  int serverLog=0;


@SerializedName("user")
@Expose
private String user;
@SerializedName("type")
@Expose
private String type;
@SerializedName("value")
@Expose
private Integer value;

  public WaterAnswer(String  user,String type,int value,String date,int serverLog){
    this.user=user;
    this.type=type;
    this.serverLog=serverLog;
    this.date=date;
    this.value=value;
  }

  public WaterAnswer(){};

  public int getServerLog() {
    return serverLog;
  }

  public void setServerLog(int serverLog) {
    this.serverLog = serverLog;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
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

public Integer getValue() {
return value;
}

public void setValue(Integer value) {
this.value = value;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("user", user).append("type", type).append("value", value).toString();
}

}
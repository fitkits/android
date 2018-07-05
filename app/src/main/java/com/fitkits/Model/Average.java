package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Average {

@SerializedName("user")
@Expose
private String user;
@SerializedName("status")
@Expose
private String status;
@SerializedName("percentage")
@Expose
private double percentage;

public String getUser() {
return user;
}

public void setUser(String user) {
this.user = user;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public double getPercentage() {
return percentage;
}

public void setPercentage(double percentage) {
this.percentage = percentage;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("user", user).append("status", status).append("percentage", percentage).toString();
}

}
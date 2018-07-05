package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Attendance {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("status")
@Expose
private Boolean status;
@SerializedName("user")
@Expose
private String user;
@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("modifiedAt")
@Expose
private String modifiedAt;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public Boolean getStatus() {
return status;
}

public void setStatus(Boolean status) {
this.status = status;
}

public String getUser() {
return user;
}

public void setUser(String user) {
this.user = user;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

public String getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(String modifiedAt) {
this.modifiedAt = modifiedAt;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("id", id).append("status", status).append("user", user).append("v", v).append("modifiedAt", modifiedAt).toString();
}

}
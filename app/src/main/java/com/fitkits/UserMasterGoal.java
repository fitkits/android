
package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class UserMasterGoal extends RealmObject {

@PrimaryKey
@SerializedName("_id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("imageURL")
@Expose
private String imageURL;

@SerializedName("dataFields")
@Expose
private RealmList<DataField> dataFields = null;
@SerializedName("enabled")
@Expose
private Boolean enabled;
@SerializedName("modifiedAt")
@Expose
private String modifiedAt;



public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getImageURL() {
return imageURL;
}

public void setImageURL(String imageURL) {
this.imageURL = imageURL;
}

public RealmList<DataField> getDataFields() {
return dataFields;
}

public void setDataFields(RealmList<DataField> dataFields) {
this.dataFields = dataFields;
}

public Boolean getEnabled() {
return enabled;
}

public void setEnabled(Boolean enabled) {
this.enabled = enabled;
}

public String getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(String modifiedAt) {
this.modifiedAt = modifiedAt;
}

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name).append("imageURL", imageURL).append("dataFields", dataFields).append("enabled", enabled).append("modifiedAt", modifiedAt).toString();
  }

}


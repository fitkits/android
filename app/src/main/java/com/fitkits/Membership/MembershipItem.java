package com.fitkits.Membership;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by akshay on 11/12/17.
 */

public class MembershipItem {
  @SerializedName("_id")
  @Expose
  private String id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("cost")
  @Expose
  private Integer cost;
  @SerializedName("expiryDays")
  @Expose
  private Integer expiryDays;

  @SerializedName("enabled")
  @Expose
  private Boolean enabled;

  @SerializedName("image")
  @Expose
  private String imageURL;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getEnabled() {
    return this.enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Integer getCost() {
    return cost;
  }

  public void setCost(Integer cost) {
    this.cost = cost;
  }

  public Integer getExpiryDays() {
    return expiryDays;
  }

  public void setExpiryDays(Integer expiryDays) {
    this.expiryDays = expiryDays;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
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
    return new ToStringBuilder(this).append("id", id).append("name", name).append("cost", cost).append("expiryDays", expiryDays).append("imageURL", imageURL).append("v", v).append("modifiedAt", modifiedAt).toString();
  }
}

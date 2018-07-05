package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Membership {
  @SerializedName("amountPaid")
  @Expose
  private int amountPaid;
 @SerializedName("membership")
 @Expose
 private String membership;
 @SerializedName("paymentID")
 @Expose
 private String paymentID;
 @SerializedName("_id")
 @Expose
 private String id;
 @SerializedName("expired")
 @Expose
 private Boolean expired;
 @SerializedName("endDate")
 @Expose
 private String endDate;
 @SerializedName("startDate")
 @Expose
 private String startDate;

  private String user;

  public Membership(String user,String membership,String paymentID) {
    this.user=user;
    this.membership = membership;
    this.paymentID=paymentID;
  }

  public Membership(String membership,int amountPaid) {
    this.membership = membership;
    this.amountPaid=amountPaid;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public int getAmountPaid() {
    return amountPaid;
  }

  public void setAmountPaid(int amountPaid) {
    this.amountPaid = amountPaid;
  }

  public String getMembership() {
   return membership;
 }

 public void setMembership(String membership) {
   this.membership = membership;
 }

 public String getPaymentID() {
   return paymentID;
 }

 public void setPaymentID(String paymentID) {
   this.paymentID = paymentID;
 }

 public String getId() {
   return id;
 }

 public void setId(String id) {
   this.id = id;
 }

 public Boolean getExpired() {
   return expired;
 }

 public void setExpired(Boolean expired) {
   this.expired = expired;
 }

 public String getEndDate() {
   return endDate;
 }

 public void setEndDate(String endDate) {
   this.endDate = endDate;
 }

 public String getStartDate() {
   return startDate;
 }

 public void setStartDate(String startDate) {
   this.startDate = startDate;
 }

 @Override
 public String toString() {
   return new ToStringBuilder(this).append("membership", membership).append("paymentID", paymentID).append("id", id).append("expired", expired).append("endDate", endDate).append("startDate", startDate).toString();
 }

}

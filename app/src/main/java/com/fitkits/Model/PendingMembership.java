package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.realm.RealmObject;

public class PendingMembership extends RealmObject {
   @SerializedName("membershipId")
   @Expose
   private String membership;
   @SerializedName("isPending")
   @Expose
   private boolean isPending;
   public PendingMembership() {
   }

   public PendingMembership(String membership, boolean isPending) {
      this.membership = membership;
      this.isPending = isPending;
   }

   public String getMembership() {
      return membership;
   }

   public void setMembership(String membership) {
      this.membership = membership;
   }

   public boolean getIsPending() {
      return isPending;
   }

   public void setIsPending(boolean isPending) {
      this.isPending = isPending;
   }

   @Override
   public String toString() {
      return new ToStringBuilder(this).append("membership",membership).append("isPending",isPending).toString();
   }
}

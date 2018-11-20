package com.fitkits.Model;

import android.util.Log;

import com.fitkits.Membership.MembershipItem;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemParent {

  @SerializedName("currentPage")
  @Expose
  private Integer currentPage;
  @SerializedName("itemsPerPage")
  @Expose
  private Integer itemsPerPage;
  @SerializedName("totalPages")
  @Expose
  private Integer totalPages;
  @SerializedName("Memberships")
  @Expose
  private List<MembershipItem> membership = null;
  @SerializedName("Subscriptions")
  @Expose
  private List<Membership> subscriptions = null;
  @SerializedName("Attendance")
  @Expose
  private List<Attendance> attendance = null;
  @SerializedName("Feeds")
  @Expose
  private List<Feed> feeds = null;
  @SerializedName("Answers")
  @Expose
  private List<WeeklyData> answers = null;
  public Integer getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
  }

  public Integer getItemsPerPage() {
    return itemsPerPage;
  }

  public void setItemsPerPage(Integer itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public List<MembershipItem> getMembership() {

    List<MembershipItem> activeMembership = new ArrayList<>();
    for(int i = 0; i < membership.size(); i++) {
      if(membership.get(i).getEnabled()) {
        Log.e("RAGHU",membership.get(i).toString());
        activeMembership.add(membership.get(i));
      }
    }
    return activeMembership;
  }

  public void setMembership(List<MembershipItem> membership) {
    this.membership = membership;
  }

  public List<Membership> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(List<Membership> subscriptions) {
    this.subscriptions = subscriptions;
  }

  public List<Attendance> getAttendance() {
    return attendance;
  }

  public void setAttendance(List<Attendance> attendance) {
    this.attendance = attendance;
  }

  public List<Feed> getFeeds() {
    return feeds;
  }

  public void setFeeds(List<Feed> feeds) {
    this.feeds = feeds;
  }

  public List<WeeklyData> getAnswers() {
    return answers;
  }

  public void setAnswers(List<WeeklyData> answers) {
    this.answers = answers;
  }
}
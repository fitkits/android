package com.fitkits.Model;

import com.fitkits.MembershipItem;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.reactivestreams.Subscription;

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
  @SerializedName("Membership")
  @Expose
  private List<MembershipItem> membership = null;
  @SerializedName("subscriptions")
  @Expose
  private List<Membership> subscriptions = null;
  @SerializedName("Attendance")
  @Expose
  private List<Attendance> attendance = null;
  @SerializedName("Feed")
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
    return membership;
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
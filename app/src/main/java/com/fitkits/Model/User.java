package com.fitkits.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class User extends RealmObject {

    @PrimaryKey
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobileNumber")
    @Expose
    private Long mobileNumber;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @SerializedName("jwt")
    @Expose
    private String jwt;
    @SerializedName("fcmRegistrationToken")
    @Expose
    private String fcmRegistrationToken;
    @SerializedName("goals")
    @Expose
    private Goals goals;
    @SerializedName("pendingMembership")
    @Expose
    private PendingMembership pendingMembership;

    @SerializedName("endurance")
    @Expose
    private RealmList<Stats> endurance;
    @SerializedName("flex")
    @Expose
    private RealmList<Stats> flex;
    @SerializedName("strength")
    @Expose
    private RealmList<Stats> strength;
    @SerializedName("images")
    @Expose
    private RealmList<Stats> images;


    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("height")
    @Expose
    private Double height;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("foodPreference")
    @Expose
    private String foodPreference;
    @SerializedName("bloodGroup")
    @Expose
    private String bloodGroup;
    @SerializedName("gender")
    @Expose
    private String gender;
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

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getFcmRegistrationToken() {
        return fcmRegistrationToken;
    }

    public void setFcmRegistrationToken(String fcmRegistrationToken) {
        this.fcmRegistrationToken = fcmRegistrationToken;
    }

    public User() {
    }

    public User(Goals goals) {
        this.goals = goals;
    }
//  public User(String bloodGroup,String gender,String dob,Integer weight,String foodPreference){
//    this.bloodGroup=bloodGroup;
//    this.gender=gender;
//    this.dob=dob;
//    this.weight=weight;
//    this.foodPreference=foodPreference;
//  }

    public User(PendingMembership pendingMembership) {
        this.pendingMembership = pendingMembership;
    }

    public User(String name, Long mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public User(String bg, String gender,
                String dob, String ht, String wt, String foodPref) {
        this.bloodGroup = bg;
        this.gender = gender;
        this.dob = dob;
        this.height = ht.isEmpty() ? 0 : Double.parseDouble(ht);
        this.weight = Integer.parseInt(wt);
        this.foodPreference = foodPref;

    }

    public User(String fcmRegistrationToken) {
        this.fcmRegistrationToken = fcmRegistrationToken;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public PendingMembership getPendingMembership() {
        return pendingMembership;
    }

    public void setPendingMembership(PendingMembership pendingMembership) {
        this.pendingMembership = pendingMembership;
    }

    public RealmList<Stats> getImages() {
        return images;
    }

    public void setImages(RealmList<Stats> images) {
        this.images = images;
    }

    public RealmList<Stats> getEndurance() {
        return endurance;
    }

    public void setEndurance(RealmList<Stats> endurance) {
        this.endurance = endurance;
    }

    public RealmList<Stats> getFlex() {
        return flex;
    }

    public void setFlex(RealmList<Stats> flex) {
        this.flex = flex;
    }

    public RealmList<Stats> getStrength() {
        return strength;
    }

    public void setStrength(RealmList<Stats> strength) {
        this.strength = strength;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getFoodPreference() {
        return foodPreference;
    }

    public void setFoodPreference(String foodPreference) {
        this.foodPreference = foodPreference;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("mobileNumber", mobileNumber).append("v", v).append("fcmRegistrationToken", fcmRegistrationToken).append("goals", goals).append("pendingMembership", pendingMembership).append("gender", gender).append("jwt", jwt).append("modifiedAt", modifiedAt).toString();
    }

}


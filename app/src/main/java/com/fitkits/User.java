package com.fitkits;

import com.fitkits.Model.Membership;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class User {

  @SerializedName("warnings")
  @Expose
  private List<Object> warnings = null;
  @SerializedName("User")
  @Expose
  private User_Profile user;

  @SerializedName("jwtToken")
  String jwtToken;

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public List<Object> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<Object> warnings) {
    this.warnings = warnings;
  }

  public User_Profile getUser() {
    return user;
  }

  public void setUser(User_Profile user) {
    this.user = user;
  }
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("warnings", warnings).append("user", user).toString();
  }
}

class Goal {

    @SerializedName("goal")
    @Expose
    private Goal_ goal;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("data")
    @Expose
    private Data data;

    public Goal(Goal_ goal,Data data){
      this.data=data;
      this.goal=goal;

    }
    public Goal_ getGoal() {
      return goal;
    }

    public void setGoal(Goal_ goal) {
      this.goal = goal;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public Data getData() {
      return data;
    }

    public void setData(Data data) {
      this.data = data;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("goal", goal).append("id", id).append("data", data).toString();
    }

  }


class User_Profile {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("mobileNumber")
    @Expose
    private Long mobileNumber;
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
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("goals")
    @Expose
    private List<Goal> goals = null;
    @SerializedName("memberships")
    @Expose
    private List<Membership> memberships = null;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("modifiedAt")
    @Expose
    private String modifiedAt;

      public User_Profile(String name, String bg, String gender,
          String dob, String ht, String wt, String foodPref) {
        this.name=name;
        this.bloodGroup=bg;
        this.gender=gender;
        this.dob=dob;
        this.height=Double.parseDouble(ht);
        this.weight=Integer.parseInt(wt);
        this.foodPreference=foodPref;

      }
      public User_Profile(String name, String mobileNumber) {
        this.name=name;
        this.mobileNumber=Long.parseLong(mobileNumber);

      }
      public User_Profile(List<Membership> memberships) {
        this.memberships=memberships;

      }
      public User_Profile(){

      }
  public User_Profile(List<Goal> goals,int a) {
    this.goals=goals;

  }


      public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public Long getMobileNumber() {
      return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
      this.mobileNumber = mobileNumber;
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

    public Integer getV() {
      return v;
    }

    public void setV(Integer v) {
      this.v = v;
    }

    public List<Goal> getGoals() {
      return goals;
    }

    public void setGoals(List<Goal> goals) {
      this.goals = goals;
    }

    public List<Membership> getMemberships() {
      return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
      this.memberships = memberships;
    }

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getModifiedAt() {
      return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
      this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("id", id).append("mobileNumber", mobileNumber).append("dob", dob).append("height", height).append("weight", weight).append("foodPreference", foodPreference).append("bloodGroup", bloodGroup).append("v", v).append("goals", goals).append("memberships", memberships).append("gender", gender).append("name", name).append("modifiedAt", modifiedAt).toString();
    }

  }


class Membership_ {

      @SerializedName("_id")
      @Expose
      private String id;
      @SerializedName("name")
      @Expose
      private String name;
      @SerializedName("cost")
      @Expose
      private Integer cost;
      @SerializedName("expiryTime")
      @Expose
      private Integer expiryDays;
      @SerializedName("imageURL")
      @Expose
      private String imageURL;
      @SerializedName("__v")
      @Expose
      private Integer v;
      @SerializedName("modifiedAt")
      @Expose
      private String modifiedAt;


       public Membership_(String id) {
         this.id = id;
       }

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





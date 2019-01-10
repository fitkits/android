package com.fitkits.Misc.Activities;


import android.os.Parcel;
import android.os.Parcelable;

public class Scores  {
    public String balanceScore,flexibilityScore,strengthScore, muscleScore, cardioScore, postureScore, lastUpdated = "";
    public Scores(String balanceScore, String flexibilityScore, String strengthScore, String muscleScore, String cardioScore, String postureScore , String lastUpdated) {
        this.balanceScore = balanceScore;
        this.flexibilityScore = flexibilityScore;
        this.strengthScore = strengthScore;
        this.muscleScore = muscleScore;
        this.cardioScore = cardioScore;
        this.postureScore = postureScore;
        this.lastUpdated = lastUpdated;

    }

}

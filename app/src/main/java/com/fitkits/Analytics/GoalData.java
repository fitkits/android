package com.fitkits.Analytics;

public class GoalData {

 String date;
 float val;
 public GoalData(String date,int val){
   this.date=date;
   this.val=val;
 }

 public String getDate() {
   return date;
 }

 public void setDate(String date) {
   this.date = date;
 }

 public float getVal() {
   return val;
 }

 public void setVal(float val) {
   this.val = val;
 }
}

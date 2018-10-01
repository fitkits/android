package com.fitkits.News.Beans;

/**
 * Created by akshay on 11/12/17.
 */

class FitkitsNewsItem {
String id;
String title;
String desc;
String coverImageURL;
FitkitsNewsItem(String id, String title,String desc, String coverImageURL){
  this.id=id;
  this.title=title;
  this.desc=desc;
  this.coverImageURL=coverImageURL;
}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getCoverImageURL() {
    return coverImageURL;
  }

  public void setCoverImageURL(String coverImageURL) {
    this.coverImageURL = coverImageURL;
  }
}

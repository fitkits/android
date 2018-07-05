package com.fitkits.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Feed {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("title")
@Expose
private String title;
@SerializedName("subTitle")
@Expose
private String subTitle;
@SerializedName("description")
@Expose
private String description;
@SerializedName("author")
@Expose
private String author;
@SerializedName("postDate")
@Expose
private String postDate;
@SerializedName("pageURL")
@Expose
private String pageURL;
@SerializedName("image")
@Expose
private String imageURL;
@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("tags")
@Expose
private List<String> tags = null;
@SerializedName("modifiedAt")
@Expose
private String modifiedAt;

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

public String getSubTitle() {
return subTitle;
}

public void setSubTitle(String subTitle) {
this.subTitle = subTitle;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getAuthor() {
return author;
}

public void setAuthor(String author) {
this.author = author;
}

public String getPostDate() {
return postDate;
}

public void setPostDate(String postDate) {
this.postDate = postDate;
}

public String getPageURL() {
return pageURL;
}

public void setPageURL(String pageURL) {
this.pageURL = pageURL;
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

public List<String> getTags() {
return tags;
}

public void setTags(List<String> tags) {
this.tags = tags;
}

public String getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(String modifiedAt) {
this.modifiedAt = modifiedAt;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("id", id).append("title", title).append("subTitle", subTitle).append("description", description).append("author", author).append("postDate", postDate).append("pageURL", pageURL).append("imageURL", imageURL).append("v", v).append("tags", tags).append("modifiedAt", modifiedAt).toString();
}

}
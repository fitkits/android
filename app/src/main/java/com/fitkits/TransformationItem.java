package com.fitkits;

/**
 * Created by akshay on 19/07/17.
 */

public class TransformationItem {
    String id;
    String imageName;
    byte[] imageUrl;
    String uploadDate;


    public TransformationItem(String id, String imageName, byte[] imageUrl, String uploadDate){
        this.id=id;
        this.imageName=imageName;
        this.imageUrl=imageUrl;
        this.uploadDate=uploadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}

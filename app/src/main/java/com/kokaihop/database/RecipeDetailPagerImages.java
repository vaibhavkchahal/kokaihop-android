package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 5/6/17.
 */
public class RecipeDetailPagerImages extends RealmObject{

    private String publicId;
    private String oldId;
    private String dateCreated;
    private String comment;
    private ImageUploader uploader;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ImageUploader getUploader() {
        return uploader;
    }

    public void setUploader(ImageUploader uploader) {
        this.uploader = uploader;
    }
}

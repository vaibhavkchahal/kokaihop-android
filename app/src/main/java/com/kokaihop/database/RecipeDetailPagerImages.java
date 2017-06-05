package com.kokaihop.database;

import io.realm.RealmObject;

/**
 * Created by Vaibhav Chahal on 5/6/17.
 */
public class RecipeDetailPagerImages extends RealmObject{

    private int publicId;
    private int oldId;
    private long dateCreated;
    private String comment;
    private ImageUploader uploader;

    public int getPublicId() {
        return publicId;
    }

    public void setPublicId(int publicId) {
        this.publicId = publicId;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
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

package com.kokaihop.database;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class SettingsRealmObject extends RealmObject {

    @SerializedName("blogYouCommented")
    private SettingsDataRealmObject blogYouCommented;

    @SerializedName("recipeYouCommented")
    private SettingsDataRealmObject recipeYouCommented;

    @SerializedName("blogComment")
    private SettingsDataRealmObject blogComment;

    @SerializedName("newPrescriptionImage")
    private SettingsDataRealmObject newPrescriptionImage;

    @SerializedName("recipeComment")
    private SettingsDataRealmObject recipeComment;

    @SerializedName("reviewedRecipe")
    private SettingsDataRealmObject reviewedRecipe;

    @SerializedName("newFollower")
    private SettingsDataRealmObject newFollower;

    @SerializedName("newMessage")
    private SettingsDataRealmObject newMessage;

    @SerializedName("newsletters")
    private boolean newsletters;

    @SerializedName("suggestionsOfTheDay")
    private boolean suggestionsOfTheDay;

    @SerializedName("notificationSoundEnabled")
    private boolean notificationSoundEnabled;
    @SerializedName("noEmails")
    private boolean noEmails;

    public SettingsDataRealmObject getBlogYouCommented() {
        return blogYouCommented;
    }

    public void setBlogYouCommented(SettingsDataRealmObject blogYouCommented) {
        this.blogYouCommented = blogYouCommented;
    }

    public SettingsDataRealmObject getRecipeYouCommented() {
        return recipeYouCommented;
    }

    public void setRecipeYouCommented(SettingsDataRealmObject recipeYouCommented) {
        this.recipeYouCommented = recipeYouCommented;
    }

    public SettingsDataRealmObject getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(SettingsDataRealmObject blogComment) {
        this.blogComment = blogComment;
    }

    public SettingsDataRealmObject getNewPrescriptionImage() {
        return newPrescriptionImage;
    }

    public void setNewPrescriptionImage(SettingsDataRealmObject newPrescriptionImage) {
        this.newPrescriptionImage = newPrescriptionImage;
    }

    public SettingsDataRealmObject getRecipeComment() {
        return recipeComment;
    }

    public void setRecipeComment(SettingsDataRealmObject recipeComment) {
        this.recipeComment = recipeComment;
    }

    public SettingsDataRealmObject getReviewedRecipe() {
        return reviewedRecipe;
    }

    public void setReviewedRecipe(SettingsDataRealmObject reviewedRecipe) {
        this.reviewedRecipe = reviewedRecipe;
    }

    public SettingsDataRealmObject getNewFollower() {
        return newFollower;
    }

    public void setNewFollower(SettingsDataRealmObject newFollower) {
        this.newFollower = newFollower;
    }

    public SettingsDataRealmObject getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(SettingsDataRealmObject newMessage) {
        this.newMessage = newMessage;
    }

    public boolean isNewsletters() {
        return newsletters;
    }

    public void setNewsletters(boolean newsletters) {
        this.newsletters = newsletters;
    }

    public boolean isSuggestionsOfTheDay() {
        return suggestionsOfTheDay;
    }

    public void setSuggestionsOfTheDay(boolean suggestionsOfTheDay) {
        this.suggestionsOfTheDay = suggestionsOfTheDay;
    }

    public boolean isNotificationSoundEnabled() {
        return notificationSoundEnabled;
    }

    public void setNotificationSoundEnabled(boolean notificationSoundEnabled) {
        this.notificationSoundEnabled = notificationSoundEnabled;
    }
    public boolean isNoEmails() {
        return noEmails;
    }

    public void setNoEmails(boolean noEmails) {
        this.noEmails = noEmails;
    }
}

package com.kokaihop.userprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

class Settings {

    @SerializedName("blogYouCommented")
    private SettingsData blogYouCommented;

    @SerializedName("recipeYouCommented")
    private SettingsData recipeYouCommented;

    @SerializedName("blogComment")
    private SettingsData blogComment;

    @SerializedName("newPrescriptionImage")
    private SettingsData newPrescriptionImage;

    @SerializedName("recipeComment")
    private SettingsData recipeComment;

    @SerializedName("reviewedRecipe")
    private SettingsData reviewedRecipe;

    @SerializedName("newFollower")
    private SettingsData newFollower;

    @SerializedName("newMessage")
    private SettingsData newMessage;

    @SerializedName("newsletters")
    private boolean newsletters;

    @SerializedName("suggestionsOfTheDay")
    private boolean suggestionsOfTheDay;

    @SerializedName("notificationSoundEnabled")
    private boolean notificationSoundEnabled;

    @SerializedName("web")
    private Web web;

    @SerializedName("noEmails")
    private boolean noEmails;

    public SettingsData getBlogYouCommented() {
        return blogYouCommented;
    }

    public void setBlogYouCommented(SettingsData blogYouCommented) {
        this.blogYouCommented = blogYouCommented;
    }

    public SettingsData getRecipeYouCommented() {
        return recipeYouCommented;
    }

    public void setRecipeYouCommented(SettingsData recipeYouCommented) {
        this.recipeYouCommented = recipeYouCommented;
    }

    public SettingsData getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(SettingsData blogComment) {
        this.blogComment = blogComment;
    }

    public SettingsData getNewPrescriptionImage() {
        return newPrescriptionImage;
    }

    public void setNewPrescriptionImage(SettingsData newPrescriptionImage) {
        this.newPrescriptionImage = newPrescriptionImage;
    }

    public SettingsData getRecipeComment() {
        return recipeComment;
    }

    public void setRecipeComment(SettingsData recipeComment) {
        this.recipeComment = recipeComment;
    }

    public SettingsData getReviewedRecipe() {
        return reviewedRecipe;
    }

    public void setReviewedRecipe(SettingsData reviewedRecipe) {
        this.reviewedRecipe = reviewedRecipe;
    }

    public SettingsData getNewFollower() {
        return newFollower;
    }

    public void setNewFollower(SettingsData newFollower) {
        this.newFollower = newFollower;
    }

    public SettingsData getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(SettingsData newMessage) {
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

    public Web getWeb() {
        return web;
    }

    public void setWeb(Web web) {
        this.web = web;
    }

    public boolean isNoEmails() {
        return noEmails;
    }

    public void setNoEmails(boolean noEmails) {
        this.noEmails = noEmails;
    }
}

package com.kokaihop.userprofile.model;

/**
 * Created by Rajendra Singh on 19/5/17.
 */

public class Settings {

    private SettingsData blogYouCommented;

    private SettingsData recipeYouCommented;

    private SettingsData blogComment;

    private SettingsData newPrescriptionImage;

    private SettingsData recipeComment;

    private SettingsData reviewedRecipe;

    private SettingsData newFollower;

    private SettingsData newMessage;

    private boolean newsletters;

    private boolean suggestionsOfTheDay;

    private boolean notificationSoundEnabled;

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

    public boolean isNoEmails() {
        return noEmails;
    }

    public void setNoEmails(boolean noEmails) {
        this.noEmails = noEmails;
    }
}

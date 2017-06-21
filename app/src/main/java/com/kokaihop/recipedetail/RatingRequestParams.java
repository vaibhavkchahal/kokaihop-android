package com.kokaihop.recipedetail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 21/6/17.
 */
public class RatingRequestParams {

    @SerializedName("recipeId")
    private String recipeId;

    @SerializedName("rating")
    private int rating;

    public RatingRequestParams(String recipeId, int rating) {
        this.recipeId = recipeId;
        this.rating = rating;
    }
}

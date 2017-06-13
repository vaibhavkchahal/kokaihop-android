package com.kokaihop.feed;

/**
 * Created by Vaibhav Chahal on 13/6/17.
 */

public class RecipeDetailPostEvent {

    private int position;
    private Recipe recipe;

    public RecipeDetailPostEvent(Recipe recipe,int position) {
        this.position = position;
        this.recipe = recipe;
    }

    public int getPosition() {
        return position;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}

package com.kokaihop.feed;

import com.kokaihop.database.RecipeRealmObject;

/**
 * Created by Vaibhav Chahal on 13/6/17.
 */

public class RecipeDetailPostEvent {

    private int position;
    private RecipeRealmObject recipe;

    public RecipeDetailPostEvent(RecipeRealmObject recipe, int position) {
        this.position = position;
        this.recipe = recipe;
    }

    public int getPosition() {
        return position;
    }

    public RecipeRealmObject getRecipe() {
        return recipe;
    }
}

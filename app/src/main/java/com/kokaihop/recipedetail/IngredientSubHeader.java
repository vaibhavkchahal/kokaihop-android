package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class IngredientSubHeader {
    private String title;

    public IngredientSubHeader(String title) {
        this.title = title;
    }

    public IngredientSubHeader(String title, long count) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


}

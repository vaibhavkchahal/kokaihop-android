package com.kokaihop.recipedetail;

import com.kokaihop.database.CookingStep;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class RecipeCookingDirection {

    private CookingStep direction;

    public RecipeCookingDirection(CookingStep direction) {
        this.direction = direction;
    }

    public CookingStep getDirection() {
        return direction;
    }

    public void setDirection(CookingStep direction) {
        this.direction = direction;
    }
}

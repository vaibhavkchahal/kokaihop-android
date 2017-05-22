package com.kokaihop.feed;

import android.widget.CheckBox;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.database.Recipe;

/**
 * Created by Vaibhav Chahal on 22/5/17.
 */

public class RecipeHandler {

    public void onCheckChangeRecipe(CheckBox checkBox, Recipe recipe) {
        if (checkBox.isChecked()) {
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_sm, 0, 0, 0);
        } else {
            checkBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike_sm, 0, 0, 0);
        }
        RecipeDataManager recipeDataManager = new RecipeDataManager();
        recipeDataManager.updateIsFavorite(checkBox.isChecked(), recipe);


    }

}

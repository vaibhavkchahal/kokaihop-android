package com.kokaihop.utility;

import com.kokaihop.cookbooks.CookbooksDataManager;
import com.kokaihop.feed.Recipe;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 11/7/17.
 */

public class RecipeUtils {

    public static int getRecipeIndexInCookbook(String userFriendlyUrl, String cookbookFriendlyUrl, String recipeId) {
        ArrayList<Recipe> recipes = new CookbooksDataManager().getRecipesOfCookbook(userFriendlyUrl, cookbookFriendlyUrl);
        if (recipes != null) {
            for (int i = 0; i < recipes.size(); i++) {
                if (recipes.get(i).get_id().equals(recipeId))
                    return i;
            }
        }
        return -1;
    }
}

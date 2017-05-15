package kokaihop.databundle;

import com.google.gson.annotations.SerializedName;
import com.kokaihop.database.Recipe;

import java.util.List;

/**
 * Created by Rajendra Singh on 5/5/17.
 */

public class SearchResponse {

    @SerializedName("searchResults")
    private List<Recipe> recipeDetailsList;
    @SerializedName("count")
    private int count;

    public List<Recipe> getRecipeDetailsList() {
        return recipeDetailsList;
    }

    public void setRecipeDetailsList(List<Recipe> recipeDetailsList) {
        this.recipeDetailsList = recipeDetailsList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

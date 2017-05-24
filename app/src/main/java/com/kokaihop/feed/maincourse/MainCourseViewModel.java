package com.kokaihop.feed.maincourse;

import android.content.Context;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.Recipe;
import com.kokaihop.feed.FeedApiHelper;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.utility.Constants.AUTHORIZATION_BEARER;


/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class MainCourseViewModel extends BaseViewModel {

    private int offset = 0;
    private int max = 20;
    private boolean isLike = true;
    private int recipeCount;
    private RecipeDataManager dataManager = null;
    private Context context;
    private boolean isDownloading;

    private List<Recipe> recipeList = new ArrayList<>();
    private List<Object> recipeListWithAdds = new ArrayList<>();

    public List<Object> getRecipeListWithAdds() {
        return recipeListWithAdds;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    @Bindable
    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
        notifyPropertyChanged(BR.downloading);
    }

    public MainCourseViewModel(Context context) {
        this.context = context;
        dataManager = new RecipeDataManager();
        fetchRecipeFromDb();
        getRecipes(getOffset(), true);
    }

    public void getRecipes(int offset, boolean isDownloading) {
        setOffset(offset);
        setDownloading(isDownloading);
        //        String authorizationToken = AUTHORIZATION_BEARER + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjM4N2FkZTFhMjU4ZjAzMDBjMzA3NGUiLCJpYXQiOjE0OTQ1NzU3Nzg3MjAsImV4cCI6MTQ5NzE2Nzc3ODcyMH0.dfZQeK4WzKiavqubA0gF4LB15sqxFBdqCQWnUQfDFaA";
        String accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        String authorizationToken = "";
        if (accessToken != null && !accessToken.isEmpty()) {
            authorizationToken = AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);

        }
        RecipeRequestParams params = new RecipeRequestParams(authorizationToken, ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY.name(), isLike, getOffset(), getMax());
        new FeedApiHelper().getRecepies(params, new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                setRecipeCount(response.getCount());
                dataManager.insertOrUpdateData(response);
                fetchRecipeFromDb();
                setDownloading(false);
            }

            @Override
            public void onFailure(String message) {
                setDownloading(false);
            }

            @Override
            public void onError(RecipeResponse response) {
                setDownloading(false);
            }
        });

    }

    public void fetchRecipeFromDb() {
        recipeList = dataManager.fetchRecipe(ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);
        recipeListWithAdds.clear();
        recipeListWithAdds.addAll(recipeList);
        int prevPos = 0;
        for (int position = 0; position < recipeListWithAdds.size(); position++) {
            if (position == 3 || (prevPos + 7) == position) {
                prevPos = position;
                AdvtDetail advtDetail = new AdvtDetail("Kokaihop");
                recipeListWithAdds.add(position, advtDetail);
            }
        }
    }


    @Override
    protected void destroy() {
    }
}

package com.kokaihop.feed;

import android.content.Context;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.Recipe;
import com.kokaihop.feed.maincourse.AdvtDetail;
import com.kokaihop.feed.maincourse.RecipeRequestParams;
import com.kokaihop.feed.maincourse.RecipeResponse;
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

public class RecipeFeedViewModel extends BaseViewModel {

    private int offset = 0;
    private int max = 20;
    private boolean isLike = true;
    private int recipeCount;
    private RecipeDataManager dataManager = null;
    private Context context;
    private boolean isDownloading;
    public static int MAX_BADGE = 60;

    private List<Recipe> recipeList = new ArrayList<>();
    private List<Object> recipeListWithAdds = new ArrayList<>();

    public List<Object> getRecipeListWithAdds() {
        return recipeListWithAdds;
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

    @Bindable
    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
        notifyPropertyChanged(BR.downloading);
    }

    public RecipeFeedViewModel(Context context, ApiConstants.BadgeType badgeType) {
        this.context = context;
        dataManager = new RecipeDataManager();
        fetchRecipeFromDb(badgeType);
        getRecipes(getOffset(), true, badgeType);
    }

    public void getRecipes(int offset, boolean isDownloading, final ApiConstants.BadgeType badgeType) {
        setOffset(offset);
        setDownloading(isDownloading);
        String accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        String authorizationToken = "";
        if (accessToken != null && !accessToken.isEmpty()) {
            authorizationToken = AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);

        }
        RecipeRequestParams params = new RecipeRequestParams(authorizationToken, badgeType.name(), isLike, getOffset(), getMax());
        new FeedApiHelper().getRecepies(params, new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                dataManager.insertOrUpdateData(response);
                fetchRecipeFromDb(badgeType);
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

    public void fetchRecipeFromDb(ApiConstants.BadgeType badgeType) {
        recipeList = dataManager.fetchRecipe(badgeType);
        recipeListWithAdds.clear();
        recipeListWithAdds.addAll(recipeList);
        addAdvtInRecipeList();
    }

    private void addAdvtInRecipeList() {
        int prevPos = 0;
        for (int position = 0; position < recipeListWithAdds.size(); position++) {
            if (position == 3 || (prevPos + 7) == position) {
                prevPos = position;
                AdvtDetail advtDetail = new AdvtDetail();
                recipeListWithAdds.add(position, advtDetail);
            }
        }
    }


    @Override
    protected void destroy() {
    }
}

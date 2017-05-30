package com.kokaihop.feed;

import android.content.Context;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.base.BaseViewModel;
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
    private boolean showProgressDialog;
    public static int MAX_BADGE = 45; //TODO: it should be 60,as QA data is not complete so we are taking it 45

    private boolean isDownloading;

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
    public boolean isShowProgressDialog() {
        return showProgressDialog;
    }

    public void setShowProgressDialog(boolean showProgressDialog) {
        this.showProgressDialog = showProgressDialog;
        notifyPropertyChanged(BR.showProgressDialog);
    }

    public RecipeFeedViewModel(Context context, ApiConstants.BadgeType badgeType) {
        this.context = context;
        dataManager = new RecipeDataManager();
        fetchRecipeFromDb(badgeType);
        getRecipes(getOffset(), true, true, badgeType);
    }

    public void getRecipes(int offset, boolean showProgressDialog, boolean isDownloading, final ApiConstants.BadgeType badgeType) {
        setOffset(offset);
        setShowProgressDialog(showProgressDialog);
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
                setShowProgressDialog(false);
                setDownloading(false);

            }

            @Override
            public void onFailure(String message) {
                setShowProgressDialog(false);
                setDownloading(false);

            }

            @Override
            public void onError(RecipeResponse response) {
                setShowProgressDialog(false);
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

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }
}

package com.kokaihop.feed;

import android.content.Context;
import android.databinding.Bindable;

import com.altaworks.kokaihop.ui.BR;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.AppCredentials;
import com.kokaihop.utility.AppUtility;
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

    public void setMax(int max) {
        this.max = max;
    }

    private int max = 20;
    private boolean isLike = true;
    private int recipeCount;
    private RecipeDataManager dataManager = null;
    private Context context;
    private boolean showProgressDialog;
    public static int MAX_BADGE = 61; //TODO: it should be 60,as QA data is not complete so we are taking it 45

    private boolean isDownloading;

    public List<RecipeRealmObject> getRecipeList() {
        return recipeList;
    }

    private List<RecipeRealmObject> recipeList = new ArrayList<>();

    public void setRecipeListWithAdds(List<Object> recipeListWithAdds) {
        this.recipeListWithAdds = recipeListWithAdds;
    }

    private List<Object> recipeListWithAdds = new ArrayList<>();
    private int firstAddPlace;
    private int itemsPerAdd;

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
        this.firstAddPlace = firstAddPlace;
        this.itemsPerAdd = itemsPerAdd;
        dataManager = new RecipeDataManager();
        fetchRecipeFromDb(badgeType);
        getRecipes(getOffset(), getMax() + 1, true, true, badgeType);
    }

    public void getRecipes(int offset, int max, boolean showProgressDialog, boolean isDownloading, final ApiConstants.BadgeType badgeType) {
        setOffset(offset);
        setShowProgressDialog(showProgressDialog);
        setDownloading(isDownloading);
        String accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        String authorizationToken = "";
        if (accessToken != null && !accessToken.isEmpty()) {
            authorizationToken = AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);

        }
        RecipeRequestParams params = new RecipeRequestParams(authorizationToken, badgeType.name(), isLike, getOffset(), max);
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
                AppUtility.showToastMessage(context,message);

            }

        });

    }

    public void fetchRecipeFromDb(ApiConstants.BadgeType badgeType) {
        recipeList = dataManager.fetchRecipe(badgeType);
        recipeListWithAdds.clear();
        recipeListWithAdds.addAll(recipeList);
        AppUtility utility = new AppUtility();
        utility.addAdvtInRecipeList(recipeListWithAdds, AppCredentials.DAILY_ADS_UNIT_IDS, context);
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

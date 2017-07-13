package com.kokaihop.feed;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.altaworks.kokaihop.ui.BR;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.utility.AppUtility.FIRST_AD_PLACE;
import static com.kokaihop.utility.AppUtility.ITEMS_PER_AD;
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

    private List<RecipeRealmObject> recipeList = new ArrayList<>();
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
        AppUtility utility = new AppUtility();
        utility.addAdvtInRecipeList(recipeListWithAdds, context);
        loadAd(FIRST_AD_PLACE);
    }

    public void loadAd(final int index) {
        if (index >= getRecipeListWithAdds().size()) {
            return;
        }

        Object item = getRecipeListWithAdds().get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }
        final AdView adView = (AdView) item;
        // Set an AdListener on the AdView to wait for the previous  ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
                // The previous  ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous  ad failed to load. Call this method again to load
                // the next ad in the items list.
                Logger.e("Ad", "The previous  ad failed to load. Attempting to"
                        + " load the next  ad in the items list.");
                adView.setVisibility(View.GONE);
                loadAd(index + ITEMS_PER_AD);
            }
        });

        // Load the ad.
        adView.loadAd(new AdRequest.Builder().addTestDevice("B2392C13860FF69BF8F847F0914A2745").build());  //TODO: Remove adTestDevice method for production

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

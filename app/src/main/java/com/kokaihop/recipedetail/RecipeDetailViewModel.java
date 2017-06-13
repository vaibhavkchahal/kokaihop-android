package com.kokaihop.recipedetail;

import android.content.Context;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.RecipeDetailPagerImages;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.AdvtDetail;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import okhttp3.ResponseBody;

/**
 * Created by Vaibhav Chahal on 31/5/17.
 */

public class RecipeDetailViewModel extends BaseViewModel {

    private final int LIMIT_COMMENT = 3;
    private final int LIMIT_SIMILAR_RECIPE = 5;
    private final DataSetListener dataSetListener;
    public RecipeRealmObject recipeRealmObject;
    private RecipeDataManager recipeDataManager;
    private String recipeID;
    private List<Object> recipeDetailItemsList = new ArrayList<>();
    private Context context;

    public RealmList<RecipeDetailPagerImages> getPagerImages() {
        return pagerImages;
    }

    RealmList<RecipeDetailPagerImages> pagerImages = new RealmList<>();

    public List<Object> getRecipeDetailItemsList() {
        return recipeDetailItemsList;
    }

    public RecipeDetailViewModel(Context context, String recipeID ,DataSetListener dataSetListener) {
        this.context = context;
        this.recipeID = recipeID;
        this.dataSetListener=dataSetListener;
        recipeDataManager = new RecipeDataManager();
        recipeRealmObject = recipeDataManager.fetchCopyOfRecipe(recipeID);
        pagerImages = recipeRealmObject.getImages();
        prepareRecipeDetailList(recipeRealmObject);
        getRecipeDetails(recipeRealmObject.getFriendlyUrl(), LIMIT_COMMENT);
    }

    private void getRecipeDetails(final String recipeFriendlyUrl, int commentToLoad) {
        setProgressVisible(true);
        new RecipeDetailApiHelper().getRecipeDetail(recipeFriendlyUrl, commentToLoad, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                try {
                    setProgressVisible(false);
                    ResponseBody responseBody = (ResponseBody) response;
                    final JSONObject json = new JSONObject(responseBody.string());
                    JSONObject recipeJSONObject = json.getJSONObject("recipe");
                    recipeDataManager.insertOrUpdateRecipeDetails(recipeJSONObject);
                    fetchSimilarRecipe(recipeFriendlyUrl, LIMIT_SIMILAR_RECIPE, recipeDataManager.fetchRecipe(recipeID).getTitle());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);

            }

            @Override
            public void onError(Object response) {
                setProgressVisible(false);

            }
        });

    }


    private void fetchSimilarRecipe(String recipeFriendlyUrl, int limit, String title) {
        new RecipeDetailApiHelper().getSimilarRecipe(recipeFriendlyUrl, limit, title, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ResponseBody responseBody = (ResponseBody) response;
                try {
                    JSONObject json = new JSONObject(responseBody.string());
                    JSONArray recipeJSONArray = json.getJSONArray("similarRecipes");
                    recipeDataManager.updateSimilarRecipe(recipeID, recipeJSONArray);
                    recipeRealmObject = recipeDataManager.fetchCopyOfRecipe(recipeID);
                    prepareRecipeDetailList(recipeRealmObject);
                    pagerImages = recipeRealmObject.getImages();
                    dataSetListener.onRecipeDetailDataUpdate();
                    dataSetListener.onPagerDataUpdate();
                    dataSetListener.onCounterUpdate();

                    Logger.i("badgeType", recipeRealmObject.getBadgeType());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }

            @Override
            public void onError(Object response) {
                setProgressVisible(false);
            }
        });

    }

    private void prepareRecipeDetailList(RecipeRealmObject recipeRealmObject) {
        recipeDetailItemsList.clear();
        String description = "";
        if (recipeRealmObject.getDescription() != null) {
            description = recipeRealmObject.getDescription().getLongDescription();
        }
        RecipeDetailHeader recipeDetailHeader = new RecipeDetailHeader(recipeRealmObject.getRating().getAverage(), recipeRealmObject.getTitle(), recipeRealmObject.getBadgeType(), description);
        recipeDetailItemsList.add(recipeDetailHeader);
        recipeDetailItemsList.add(new AdvtDetail());
        addIngredients(recipeRealmObject);
        recipeDetailItemsList.add(new RecipeQuantityVariator(recipeRealmObject.getServings()));
        recipeDetailItemsList.add(new AdvtDetail());
        recipeDetailItemsList.add(new ListHeading(context.getString(R.string.text_directions)));

        for (int i = 0; i < recipeRealmObject.getCookingSteps().size(); i++) {
            recipeDetailItemsList.add(new RecipeCookingDirection(recipeRealmObject.getCookingSteps().get(i)));
        }
        RecipeSpecifications recipeSpecifications = getRecipeSpecifications(recipeRealmObject);
        recipeDetailItemsList.add(recipeSpecifications);
        addComments(recipeRealmObject);
        addSimilarRecipies(recipeRealmObject);

    }

    private void addIngredients(RecipeRealmObject recipeRealmObject) {
        recipeDetailItemsList.add(new ListHeading(context.getString(R.string.text_Ingredients)));
        for (int i = 0; i < recipeRealmObject.getIngredients().size(); i++) {
            IngredientsRealmObject ingredientsRealmObject = recipeRealmObject.getIngredients().get(i);
            if (ingredientsRealmObject.getAmount() != 0)
                recipeDetailItemsList.add(ingredientsRealmObject);
            if (ingredientsRealmObject.isHeader()) {
                recipeDetailItemsList.add(new IngredientSubHeader(ingredientsRealmObject.getName()));
            }
        }
    }

    private void addSimilarRecipies(RecipeRealmObject recipeRealmObject) {
        recipeDetailItemsList.add(new ListHeading(context.getString(R.string.text_SimilarRecipies)));
        for (int i = 0; i < recipeRealmObject.getSimilarRecipes().size(); i++) {
            RecipeRealmObject realmObject = recipeRealmObject.getSimilarRecipes().get(i);
            String mainImageUrl = "0";
            if (realmObject.getCoverImage() != null) {
                mainImageUrl = realmObject.getCoverImage();
            } else if (realmObject.getMainImage() != null) {
                mainImageUrl = realmObject.getMainImage().getPublicId();
            }
            String profileImageUrl = "0";
            if (realmObject.getCreatedBy().getProfileImageId() != null) {
                profileImageUrl = realmObject.getCreatedBy().getProfileImageId();
            }
            SimilarRecipe similarRecipe = new SimilarRecipe(realmObject.getTitle(), mainImageUrl, profileImageUrl, realmObject.getCreatedBy().getName());
            recipeDetailItemsList.add(similarRecipe);
        }
    }

    private void addComments(RecipeRealmObject recipeRealmObject) {
        recipeDetailItemsList.add(new ListHeading(context.getString(R.string.text_comments), recipeRealmObject.getCounter().getComments()));
        for (int i = 0; i < recipeRealmObject.getComments().size(); i++) {
            recipeDetailItemsList.add(recipeRealmObject.getComments().get(i));
        }
        recipeDetailItemsList.add(new ListHeading(context.getString(R.string.add_comments)));

    }

    private RecipeSpecifications getRecipeSpecifications(RecipeRealmObject recipeRealmObject) {
        RecipeSpecifications specifications = new RecipeSpecifications();
        specifications.setName(recipeRealmObject.getCreatedBy().getName());
        specifications.setImageId(recipeRealmObject.getCreatedBy().getProfileImageId());
        specifications.setDateCreated(Long.parseLong(recipeRealmObject.getDateCreated()));
        if (recipeRealmObject.getCookingMethod() != null) {
            specifications.setCategory1(recipeRealmObject.getCookingMethod().getName());
        }
        if (recipeRealmObject.getCuisine() != null) {
            specifications.setCategory2(recipeRealmObject.getCuisine().getName());
        }
        if (recipeRealmObject.getCategory() != null) {
            specifications.setCategory3(recipeRealmObject.getCategory().getName());
        }
        specifications.setViewerCount(recipeRealmObject.getCounter().getViewed());
        specifications.setPrinted(recipeRealmObject.getCounter().getPrinted());
        specifications.setAddToCollections(recipeRealmObject.getCounter().getAddedToCollection());
        return specifications;
    }

    public String getRecipeImageId() {
        return recipeRealmObject.getCreatedBy().getProfileImageId();
    }

    @Override
    protected void destroy() {
    }


    public interface DataSetListener {
        void onPagerDataUpdate();
        void onRecipeDetailDataUpdate();
        void onCounterUpdate();
    }
}

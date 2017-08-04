package com.kokaihop.recipedetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.RecipeDetailPagerImages;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.userprofile.OtherUserProfileActivity;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.AppCredentials;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SharedPrefUtils;
import com.kokaihop.utility.UploadImageAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    private String friendlyUrl, recipeId;
    private List<Object> recipeDetailItemsList = new ArrayList<>();
    private Context context;
    private JSONObject copyJsonObject;
    private JSONObject collectionMapping;
    final static int ADD_TO_COOKBOOK_REQ_CODE = 10;
    private RealmList<RecipeDetailPagerImages> pagerImages = new RealmList<>();

    public RealmList<RecipeDetailPagerImages> getPagerImages() {
        return pagerImages;
    }

    public List<Object> getRecipeDetailItemsList() {
        return recipeDetailItemsList;
    }

    public RecipeDetailViewModel(Context context, String recipeId, String friendlyUrl, DataSetListener dataSetListener) {
        this.context = context;
        this.friendlyUrl = friendlyUrl;
        this.recipeId = recipeId;
        this.dataSetListener = dataSetListener;
        recipeDataManager = new RecipeDataManager();
        if (friendlyUrl == null) {
            recipeRealmObject = recipeDataManager.fetchRecipe(recipeId);
            if (recipeRealmObject != null)
                this.friendlyUrl = recipeRealmObject.getFriendlyUrl();
        } else {
            recipeRealmObject = recipeDataManager.fetchRecipeUsingFriendlyUrl(friendlyUrl);
            this.recipeId = recipeRealmObject.get_id();

        }
        pagerImages.clear();
        if (recipeRealmObject != null) {
            pagerImages.addAll(recipeRealmObject.getImages());
        }
        prepareRecipeDetailList(recipeRealmObject);
        getRecipeDetails(recipeRealmObject.getFriendlyUrl(), LIMIT_COMMENT);
    }

    public void getRecipeDetails() {
        if (copyJsonObject != null)
            getRecipeDetails(recipeRealmObject.getFriendlyUrl(), LIMIT_COMMENT);
    }

    private void getRecipeDetails(final String recipeFriendlyUrl, int commentToLoad) {
        setProgressVisible(true);
        String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        new RecipeDetailApiHelper().getRecipeDetail(accessToken, recipeFriendlyUrl, commentToLoad, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                try {
                    setProgressVisible(false);
                    ResponseBody responseBody = (ResponseBody) response;
                    JSONObject responseJSON = new JSONObject(responseBody.string());
                    final JSONObject recipeJSONObject = responseJSON.getJSONObject("recipe");
                    collectionMapping = responseJSON.getJSONObject(Constants.COLLECTION_MAPPING);
                    copyJsonObject = new JSONObject(recipeJSONObject.toString());
                    recipeJSONObject.put("friendlyUrl", recipeFriendlyUrl);
                    recipeDataManager.insertOrUpdateRecipeDetails(recipeJSONObject);
                    String title = "";
                    RecipeRealmObject recipe = recipeDataManager.fetchRecipeUsingFriendlyUrl(recipeFriendlyUrl);
                    if (recipe == null) {
                        recipe = recipeDataManager.fetchRecipe(recipeId);
                    }
                    if (recipe != null) {
                        title = recipe.getTitle();
                    }
                    fetchSimilarRecipe(recipeFriendlyUrl, LIMIT_SIMILAR_RECIPE, title);
                } catch (JSONException | IOException e) {
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
                    recipeDataManager.updateSimilarRecipe(friendlyUrl, recipeJSONArray);
                    recipeRealmObject = recipeDataManager.fetchCopyOfRecipeByFriendlyUrl(friendlyUrl);
                    prepareRecipeDetailList(recipeRealmObject);
                    pagerImages.clear();
                    pagerImages.addAll(recipeRealmObject.getImages());
                    dataSetListener.onRecipeDetailDataUpdate();
                    dataSetListener.onPagerDataUpdate();
                    dataSetListener.onCounterUpdate();
                    Logger.i("badgeType", recipeRealmObject.getBadgeType());
                } catch (JSONException | IOException e) {
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
        float rating = 0.0f;
        if (recipeRealmObject.getRating() != null) {
            rating = recipeRealmObject.getRating().getAverage();
        }
        String creatorFriendlyUrl = "";
        if (recipeRealmObject.getCreatedBy() != null) {
            creatorFriendlyUrl = recipeRealmObject.getCreatedBy().getFriendlyUrl();
        }
        RecipeDetailHeader recipeDetailHeader = new RecipeDetailHeader(rating, recipeRealmObject.getTitle(), recipeRealmObject.getBadgeType(), description, creatorFriendlyUrl);
        recipeDetailHeader.setRecipeId(recipeRealmObject.get_id());
        recipeDetailItemsList.add(recipeDetailHeader);
        AdView adViewBanner = new AdView(context);
        adViewBanner.setAdSize(AdSize.LARGE_BANNER); //320x100 LARGE_BANNER
        adViewBanner.setAdUnitId(AppCredentials.RECIPE_DETAILS_ADS_UNIT_IDS[0]);
        recipeDetailItemsList.add(adViewBanner);
        addIngredients(recipeRealmObject);
        recipeDetailItemsList.add(new RecipeQuantityVariator(recipeRealmObject.getServings()));
        AdView adViewRectangle = new AdView(context);
        adViewRectangle.setAdSize(AdSize.MEDIUM_RECTANGLE); //320x250 medium rectangle
        adViewRectangle.setAdUnitId(AppCredentials.RECIPE_DETAILS_ADS_UNIT_IDS[1]);
        recipeDetailItemsList.add(adViewRectangle);
        addCookingDirections(recipeRealmObject);
        RecipeSpecifications recipeSpecifications = getRecipeSpecifications(recipeRealmObject);
        recipeDetailItemsList.add(recipeSpecifications);
        addComments(recipeRealmObject);
        addSimilarRecipies(recipeRealmObject);

    }

    private void addCookingDirections(RecipeRealmObject recipeRealmObject) {
        if (!recipeRealmObject.getCookingSteps().isEmpty()) {
            recipeDetailItemsList.add(new ListHeading(context.getString(R.string.text_directions)));
            for (int i = 0; i < recipeRealmObject.getCookingSteps().size(); i++) {
                recipeDetailItemsList.add(new RecipeCookingDirection(recipeRealmObject.getCookingSteps().get(i)));
            }
        }
    }


    private void addIngredients(RecipeRealmObject recipeRealmObject) {
        if (!recipeRealmObject.getIngredients().isEmpty()) {
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
    }

    private void addSimilarRecipies(RecipeRealmObject recipeRealmObject) {
        if (!recipeRealmObject.getSimilarRecipes().isEmpty()) {
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
                String createdByName = "";
                if (realmObject.getCreatedBy() != null) {
                    profileImageUrl = realmObject.getCreatedBy().getProfileImageId();
                    createdByName = realmObject.getCreatedBy().getName();

                }
                SimilarRecipe similarRecipe = new SimilarRecipe(realmObject.get_id(), realmObject.getTitle(), mainImageUrl, profileImageUrl, createdByName);
                recipeDetailItemsList.add(similarRecipe);
            }
        }
    }

    private void addComments(RecipeRealmObject recipeRealmObject) {
        ListHeading commentsHeading = new ListHeading(context.getString(R.string.text_comments));
        if (recipeRealmObject.getCounter() != null)
            commentsHeading.setCommentCount(recipeRealmObject.getCounter().getComments());
        commentsHeading.setRecipeId(recipeRealmObject.get_id());
        commentsHeading.setFriendlyUrl(recipeRealmObject.getFriendlyUrl());
        recipeDetailItemsList.add(commentsHeading);
        for (int i = 0; (i < recipeRealmObject.getComments().size()) && (i < 3); i++) {
            recipeDetailItemsList.add(recipeRealmObject.getComments().get(i));
        }
        ListHeading addCommentsHeading = new ListHeading(context.getString(R.string.add_comments));
        addCommentsHeading.setRecipeId(recipeRealmObject.get_id());
        addCommentsHeading.setFriendlyUrl(recipeRealmObject.getFriendlyUrl());
        recipeDetailItemsList.add(addCommentsHeading);

    }

    private RecipeSpecifications getRecipeSpecifications(RecipeRealmObject recipeRealmObject) {
        RecipeSpecifications specifications = new RecipeSpecifications();
        if (recipeRealmObject.getCreatedBy() != null) {
            specifications.setName(recipeRealmObject.getCreatedBy().getName());
            specifications.setImageId(recipeRealmObject.getCreatedBy().getProfileImageId());
            if (recipeRealmObject.getDateCreated() != null) {
                specifications.setDateCreated(Long.parseLong(recipeRealmObject.getDateCreated()));
            }
        }
        if (recipeRealmObject.getCookingMethod() != null) {
            specifications.setCategory1(recipeRealmObject.getCookingMethod().getName());
            specifications.setCategory1FriendlyUrl(recipeRealmObject.getCookingMethod().getFriendlyUrl());
        }
        if (recipeRealmObject.getCuisine() != null) {
            specifications.setCategory2(recipeRealmObject.getCuisine().getName());
            specifications.setCategory2FriendlyUrl(recipeRealmObject.getCuisine().getFriendlyUrl());
        }
        if (recipeRealmObject.getCategory() != null) {
            specifications.setCategory3(recipeRealmObject.getCategory().getName());
            specifications.setCategory3FriendlyUrl(recipeRealmObject.getCategory().getFriendlyUrl());
        }
        if (recipeRealmObject.getCreatedBy() != null) {
            specifications.setUserId(recipeRealmObject.getCreatedBy().getId());
            specifications.setFriendlyUrl(recipeRealmObject.getCreatedBy().getFriendlyUrl());
        }
        if (recipeRealmObject.getCounter() != null) {
            specifications.setViewerCount(recipeRealmObject.getCounter().getViewed());
            specifications.setPrinted(recipeRealmObject.getCounter().getPrinted());
            specifications.setAddToCollections(recipeRealmObject.getCounter().getAddedToCollection());
        }
        return specifications;
    }

    public String getRecipeImageId() {
        String profileImageId = "";
        if (recipeRealmObject.getCreatedBy() != null) {
            profileImageId = recipeRealmObject.getCreatedBy().getProfileImageId();
        }
        return profileImageId;
    }

    public String getRecipeFriendlyUrl() {
        return recipeRealmObject.getFriendlyUrl();
    }

    public String getRecipeTitle() {
        return recipeRealmObject.getTitle();
    }

    public Recipe getRecipe(RecipeRealmObject recipeRealmObject) {
        return recipeDataManager.getRecipe(recipeRealmObject);
    }

    public CheckBox getCheckBox() {
        return new CheckBox(context);
    }

    public void openCookBookScreen() {
        Intent i = new Intent(context, AddToCookBookActivity.class);
        i.putExtra(Constants.RECIPE_ID, recipeId);
        if (collectionMapping != null) {
            i.putExtra(Constants.COLLECTION_MAPPING, collectionMapping.toString());
        }
        ((Activity) context).startActivityForResult(i, ADD_TO_COOKBOOK_REQ_CODE);
    }

    public void updateComments() {
        RecipeRealmObject recipeRealmObject = recipeDataManager.fetchCopyOfRecipeByFriendlyUrl(friendlyUrl);
        prepareRecipeDetailList(recipeRealmObject);
        dataSetListener.onRecipeDetailDataUpdate();
    }

    @Override
    protected void destroy() {
    }

    public interface DataSetListener {
        void onPagerDataUpdate();

        void onRecipeDetailDataUpdate();

        void onCounterUpdate();
    }

    public void uploadImageOnCloudinary(final String imagePath) {

        GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.photo_upload_category), context.getString(R.string.photo_upload_action), context.getString(R.string.recipe_photo_upload_label));

        HashMap<String, String> paramMap = CloudinaryUtils.getCloudinaryParams(imagePath);
        setProgressVisible(true);
        UploadImageAsync uploadImageAsync = new UploadImageAsync(context, paramMap, new UploadImageAsync.OnCompleteListener() {
            @Override
            public void onComplete(Map<String, String> uploadResult) throws ParseException {
                JSONArray images = new JSONArray();
                if (copyJsonObject != null) {
                    try {
                        if (copyJsonObject.has("recipe")) {
                            copyJsonObject = copyJsonObject.getJSONObject("recipe");
                        }
                        if (copyJsonObject.has("images")) {
                            images = copyJsonObject.getJSONArray("images");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                jsonObject.getJSONObject("")
                    JSONObject image = new JSONObject();
                    JSONObject uploader = new JSONObject();
                    try {
                        User user = User.getInstance();
                        uploader.put("id", SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID));
                        uploader.put("friendlyUrl", user.getFriendlyUrl());
                        if (user.getName() != null) {
                            uploader.put("name", user.getName().getFull());
                        }
                        if (user.getProfileImage() != null) {
                            uploader.put("profileImageId", user.getProfileImage().getCloudinaryId());
                        }
                        if (uploadResult != null) {
                            image.put("dateCreated", new Date().getTime());
                            image.put("publicId", uploadResult.get("public_id"));
                            image.put("uploader", uploader);
                            image.put("new", true);
                            images.put(image);
                            copyJsonObject.put("images", images);
                            Logger.d("imageUpload", copyJsonObject.toString());
                            final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), copyJsonObject.toString());
                            String accessToken = Constants.AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
                            new RecipeDetailApiHelper().updateRecipeDetail(accessToken, recipeRealmObject.get_id(), requestBody, new IApiRequestComplete() {
                                @Override
                                public void onSuccess(Object response) {
                                    GoogleAnalyticsHelper.trackEventAction(context.getString(R.string.photo_upload_category), context.getString(R.string.uploaded_photo_action), context.getString(R.string.recipe_photo_uploaded_label));

                                    try {
                                        Logger.d("Upload Image", ((ResponseBody) response).string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Logger.e("image upload", "success " + response.toString());
                                    Toast.makeText(context, context.getString(R.string.recipe_image_upload_success), Toast.LENGTH_SHORT).show();
//                                dataSetListener.onPagerDataUpdate();
                                    getRecipeDetails(recipeRealmObject.getFriendlyUrl(), LIMIT_COMMENT);
                                }

                                @Override
                                public void onFailure(String message) {
                                    Logger.e("image upload", message);
                                    Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
                                    setProgressVisible(false);
                                }

                                @Override
                                public void onError(Object response) {
                                    Logger.e("image upload", "failure " + response.toString());
                                    Toast.makeText(context, context.getString(R.string.recipe_image_upload_failed), Toast.LENGTH_SHORT).show();
                                    setProgressVisible(false);
                                }
                            });
                        } else {
                            Toast.makeText(context, context.getString(R.string.recipe_image_upload_failed), Toast.LENGTH_SHORT).show();
                            setProgressVisible(false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        setProgressVisible(false);
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.check_intenet_connection), Toast.LENGTH_SHORT).show();
                    setProgressVisible(false);
                }
            }
        });
        uploadImageAsync.execute();

    }


    public void openUserProfile(View view) {
        Intent i = new Intent(context, OtherUserProfileActivity.class);
        i.putExtra(Constants.USER_ID, recipeRealmObject.getCreatedBy().getId());
        i.putExtra(Constants.FRIENDLY_URL, recipeRealmObject.getCreatedBy().getFriendlyUrl());
        (context).startActivity(i);
    }

    public RecipeRealmObject getRecipeRealmObject() {
        return recipeRealmObject;
    }
}

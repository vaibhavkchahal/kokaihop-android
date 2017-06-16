package com.kokaihop.feed;

import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.database.CounterRealmObject;
import com.kokaihop.database.RecipeInfo;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeDataManager {
    private Realm realm;

    private static final String RECIPE_ID = "_id";

    public RecipeDataManager() {
        realm = Realm.getDefaultInstance();
    }

    public List<Recipe> fetchRecipe(ApiConstants.BadgeType badgeType) {
        RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class)
                .equalTo("badgeType", badgeType.value)
                .findAllSorted("badgeDateCreated", Sort.DESCENDING);
        List<Recipe> recipeList = new ArrayList<>(recipeRealmObjectList.size());
        for (RecipeRealmObject recipeRealmObject : recipeRealmObjectList) {
            recipeList.add(getRecipe(recipeRealmObject));
        }
        return recipeList;
    }

    public Recipe getRecipe(RecipeRealmObject recipeRealmObject) {
        Recipe recipe = new Recipe();
        recipe.set_id(recipeRealmObject.get_id());
        recipe.setTitle(recipeRealmObject.getTitle());
        recipe.setType(recipeRealmObject.getType());
        recipe.setCreatedById(recipeRealmObject.getCreatedBy().getId());
        recipe.setCreatedByName(recipeRealmObject.getCreatedBy().getName());
        recipe.setCreatedByProfileImageId(recipeRealmObject.getCreatedBy().getProfileImageId());
        recipe.setCoverImage(recipeRealmObject.getCoverImage());
        if (recipeRealmObject.getMainImage() != null) {
            recipe.setMainImagePublicId(recipeRealmObject.getMainImage().getPublicId());
        }
        recipe.setFavorite(recipeRealmObject.isFavorite());
        recipe.setLikes(String.valueOf(recipeRealmObject.getCounter().getLikes()));
        if (recipeRealmObject.getRating() != null) {
            recipe.setRatingAverage(recipeRealmObject.getRating().getAverage());

        }
        recipe.setBadgeDateCreated(recipeRealmObject.getBadgeDateCreated());
        recipe.setComments(recipeRealmObject.getCounter().getComments());
        recipe.setBadgeType(recipeRealmObject.getBadgeType());
        recipe.setLastUpdated(recipeRealmObject.getLastUpdated());
        return recipe;

    }


    public void insertOrUpdateData(RecipeResponse recipeResponse) {
        realm.beginTransaction();
        List<RecipeRealmObject> recipeRealmObjectList = new ArrayList<>();
        for (RecipeInfo recipeInfo : recipeResponse.getRecipeDetailsList()) {
            RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                    .equalTo(RECIPE_ID, recipeInfo.getRecipeRealmObject().get_id()).findFirst();
            if (recipeRealmObject != null) {
                recipeRealmObject.setBadgeType(recipeInfo.getRecipeRealmObject().getBadgeType());
                recipeRealmObject.setCounter(updateCounter(recipeInfo.getRecipeRealmObject()));
                recipeRealmObject.setBadgeDateCreated(Long.parseLong(recipeInfo.getRecipeRealmObject().getDateCreated()));
                recipeRealmObject.setCoverImage(recipeInfo.getRecipeRealmObject().getCoverImage());
                if (recipeResponse.getMyLikes() != null) {
                    boolean isLiked = recipeResponse.getMyLikes().contains(recipeRealmObject.get_id());
                    recipeRealmObject.setFavorite(isLiked);
                }
            } else {
                recipeRealmObject = recipeInfo.getRecipeRealmObject();
            }
            recipeRealmObject.setLastUpdated(System.currentTimeMillis());
            recipeRealmObjectList.add(recipeRealmObject);
        }
        realm.insertOrUpdate(recipeRealmObjectList);
//        recipeDataListener.onTransactionComplete(true);
        realm.commitTransaction();
    }

    private CounterRealmObject updateCounter(RecipeRealmObject recipeRealmObject) {
        CounterRealmObject counterRealmObject = realm.createObject(CounterRealmObject.class);
        CounterRealmObject counterRealmObjectTemp = recipeRealmObject.getCounter();
        counterRealmObject.setAddedToCollection(counterRealmObjectTemp.getAddedToCollection());
        counterRealmObject.setComments(counterRealmObjectTemp.getComments());
        counterRealmObject.setLikes(counterRealmObjectTemp.getLikes());
        counterRealmObject.setMail(counterRealmObjectTemp.getMail());
        counterRealmObject.setPrinted(counterRealmObjectTemp.getPrinted());
        counterRealmObject.setViewed(counterRealmObjectTemp.getViewed());
        return counterRealmObject;
    }

    public void updateIsFavoriteInDB(final boolean checked, final Recipe recipe) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo(RECIPE_ID, recipe.get_id()).findFirst();
                recipeRealmObject.setFavorite(checked);
            }
        });
    }


    public void updateLikesCount(final Recipe recipe, final long likes) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo(RECIPE_ID, recipe.get_id()).findFirst();
                recipeRealmObject.getCounter().setLikes(likes);

            }
        });
    }


    public void insertOrUpdateRecipeDetails(final JSONObject jsonObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    if (jsonObject.has("cookingSteps")) {
                        JSONArray cookingStepsJSONArray = jsonObject.getJSONArray("cookingSteps");
                        JSONArray updatedJSONArray = new JSONArray();
                        for (int i = 0; i < cookingStepsJSONArray.length(); i++) {
                            String step = cookingStepsJSONArray.getString(i);
                            JSONObject stepJSONObject = new JSONObject();
                            stepJSONObject.put("step", step);
                            stepJSONObject.put("serialNo", i + 1);
                            updatedJSONArray.put(stepJSONObject);
                        }
                        jsonObject.remove("cookingSteps");
                        jsonObject.put("cookingSteps", updatedJSONArray);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                realm.createOrUpdateObjectFromJson(RecipeRealmObject.class, jsonObject);
            }
        });
    }


    public void insertOrUpdateRecipe(final JSONArray jsonObject) {
        final JSONObject recipeJSONObject;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(RecipeRealmObject.class, jsonObject);
            }
        });
    }

    public RecipeRealmObject fetchRecipe(String recipeID) {
        //return the managed object
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo(RECIPE_ID, recipeID).findFirst();
        return recipeRealmObject;
    }

    public RecipeRealmObject fetchCopyOfRecipe(String recipeID) {
        //        //return the unmanaged object
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo(RECIPE_ID, recipeID).findFirst();
        return realm.copyFromRealm(recipeRealmObject);
    }

    public void updateSimilarRecipe(final String recipeID, final JSONArray jsonArray) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo(RECIPE_ID, recipeID).findFirst();
                RealmList<RecipeRealmObject> similarRecipes = new RealmList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject recipeJSONObject = (JSONObject) jsonArray.get(i);
                        Logger.d("jsonArray", jsonArray.toString());
                        RecipeRealmObject similarRecipe = realm.createOrUpdateObjectFromJson(RecipeRealmObject.class, recipeJSONObject);
                        similarRecipes.add(similarRecipe);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    recipeRealmObject.setSimilarRecipes(similarRecipes);
                }
            }
        });
    }

    public void updateRecipeCommentList(final String recipeID, final JSONArray jsonArray) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo(RECIPE_ID, recipeID).findFirst();
                RealmList<CommentRealmObject> commentRealmObjects = new RealmList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject recipeJSONObject = (JSONObject) jsonArray.get(i);
                        Logger.d("jsonArray", jsonArray.toString());
                        CommentRealmObject commentRealmObject = realm.createOrUpdateObjectFromJson(CommentRealmObject.class, recipeJSONObject);
                        commentRealmObjects.add(commentRealmObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recipeRealmObject.getComments().addAll(commentRealmObjects);
            }
        });
    }
}



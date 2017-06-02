package com.kokaihop.feed;

import com.kokaihop.database.CategoryRealmObject;
import com.kokaihop.database.CookingMethodRealmObject;
import com.kokaihop.database.CounterRealmObject;
import com.kokaihop.database.CreatedByRealmObject;
import com.kokaihop.database.CuisineRealmObject;
import com.kokaihop.database.RecipeInfo;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.utility.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeDataManager {
    private Realm realm;

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

    private Recipe getRecipe(RecipeRealmObject recipeRealmObject) {
        Recipe recipe = new Recipe();
        recipe.set_id(recipeRealmObject.get_id());
        recipe.setTitle(recipeRealmObject.getTitle());
        recipe.setType(recipeRealmObject.getType());
        recipe.setCreatedById(recipeRealmObject.getCreatedByRealmObject().getId());
        recipe.setCreatedByName(recipeRealmObject.getCreatedByRealmObject().getName());
        recipe.setCreatedByProfileImageId(recipeRealmObject.getCreatedByRealmObject().getProfileImageId());
        if (recipeRealmObject.getMainImageRealmObject() != null) {
            recipe.setMainImagePublicId(recipeRealmObject.getMainImageRealmObject().getPublicId());
        }
        recipe.setFavorite(recipeRealmObject.isFavorite());
        recipe.setLikes(String.valueOf(recipeRealmObject.getCounterRealmObject().getLikes()));
        if (recipeRealmObject.getRatingRealmObject() != null) {
            recipe.setRatingAverage(recipeRealmObject.getRatingRealmObject().getAverage());

        }
        recipe.setBadgeDateCreated(recipeRealmObject.getBadgeDateCreated());
        recipe.setComments(recipeRealmObject.getCounterRealmObject().getComments());
        recipe.setBadgeType(recipeRealmObject.getBadgeType());
        recipe.setLastUpdated(recipeRealmObject.getLastUpdated());


        return recipe;

    }


    public void insertOrUpdateData(RecipeResponse recipeResponse) {
        realm.beginTransaction();
        List<RecipeRealmObject> recipeRealmObjectList = new ArrayList<>();
        for (RecipeInfo recipeInfo : recipeResponse.getRecipeDetailsList()) {
            RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                    .equalTo("_id", recipeInfo.getRecipeRealmObject().get_id()).findFirst();
            if (recipeRealmObject != null) {
                recipeRealmObject.setBadgeType(recipeInfo.getRecipeRealmObject().getBadgeType());
                recipeRealmObject.setCounterRealmObject(updateCounter(recipeInfo.getRecipeRealmObject()));
                recipeRealmObject.setBadgeDateCreated(recipeInfo.getRecipeRealmObject().getDateCreated());
//                recipeRealmObject.setCookingMethodRealmObject(updateCooking(recipeInfo.getRecipeRealmObject()));
//                recipeRealmObject.setCuisineRealmObject(updateCuisineObject(recipeInfo.getRecipeRealmObject()));
//                recipeRealmObject.setCategoryRealmObject(updateCategoryObject(recipeInfo.getRecipeRealmObject()));

//                recipeRealmObject.setCreatedByRealmObject(updateCreatedByObject(recipeInfo.getRecipeRealmObject()));

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

    private CreatedByRealmObject updateCreatedByObject(RecipeRealmObject recipeRealmObject) {
        CreatedByRealmObject createdByRealmObject = realm.createObject(CreatedByRealmObject.class);
        CreatedByRealmObject createdByRealmObjectTemp = recipeRealmObject.getCreatedByRealmObject();
        createdByRealmObject.setName(createdByRealmObjectTemp.getName());
        createdByRealmObject.setId(createdByRealmObjectTemp.getId());
        createdByRealmObject.setFriendlyUrl(createdByRealmObjectTemp.getFriendlyUrl());
        createdByRealmObject.setProfileImageId(createdByRealmObjectTemp.getProfileImageId());
        return createdByRealmObject;
    }

    private CategoryRealmObject updateCategoryObject(RecipeRealmObject recipeRealmObject) {
        CategoryRealmObject categoryRealmObject = realm.createObject(CategoryRealmObject.class);
        CategoryRealmObject categoryRealmObjectTemp = recipeRealmObject.getCategoryRealmObject();
        categoryRealmObject.setName(categoryRealmObjectTemp.getName());
        categoryRealmObject.setId(categoryRealmObjectTemp.getId());
        categoryRealmObject.setFriendlyUrl(categoryRealmObjectTemp.getFriendlyUrl());
        return categoryRealmObject;
    }


    private CuisineRealmObject updateCuisineObject(RecipeRealmObject recipeRealmObject) {
        CuisineRealmObject cuisineRealmObject = realm.createObject(CuisineRealmObject.class);
        CuisineRealmObject cuisineRealmObjectTemp = recipeRealmObject.getCuisineRealmObject();
        cuisineRealmObject.setName(cuisineRealmObjectTemp.getName());
        cuisineRealmObject.setId(cuisineRealmObjectTemp.getId());
        cuisineRealmObject.setOldId(cuisineRealmObjectTemp.getOldId());
        return cuisineRealmObject;
    }

    private CookingMethodRealmObject updateCooking(RecipeRealmObject recipeRealmObject) {
        CookingMethodRealmObject cookingMethodRealmObject = realm.createObject(CookingMethodRealmObject.class);
        CookingMethodRealmObject cookingMethodRealmObjectTemp = recipeRealmObject.getCookingMethodRealmObject();
        cookingMethodRealmObject.setName(cookingMethodRealmObjectTemp.getName());
        cookingMethodRealmObject.setId(cookingMethodRealmObjectTemp.getId());
        return cookingMethodRealmObject;
    }

    private CounterRealmObject updateCounter(RecipeRealmObject recipeRealmObject) {
        CounterRealmObject counterRealmObject = realm.createObject(CounterRealmObject.class);
        CounterRealmObject counterRealmObjectTemp = recipeRealmObject.getCounterRealmObject();
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
                        .equalTo("_id", recipe.get_id()).findFirst();
                recipeRealmObject.setFavorite(checked);
            }
        });
    }


    public void updateLikesCount(final Recipe recipe, final long likes) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo("_id", recipe.get_id()).findFirst();
                recipeRealmObject.getCounterRealmObject().setLikes(likes);

            }
        });
    }


    public void insertOrUpdateRecipeDetails(JSONObject jsonObject) {
        final JSONObject recipeJSONObject;
        try {
            recipeJSONObject = jsonObject.getJSONObject("recipe");
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.createOrUpdateObjectFromJson(RecipeRealmObject.class, recipeJSONObject);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public RecipeRealmObject fetchRecipe(String recipeID) {
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo("_id", recipeID).findFirst();
        return recipeRealmObject;
    }

}



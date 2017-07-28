package com.kokaihop.feed;

import com.kokaihop.database.CommentRealmObject;
import com.kokaihop.database.CounterRealmObject;
import com.kokaihop.database.RecipeInfo;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.JSONObjectUtility;
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

import static com.kokaihop.utility.Constants.FRIENDLY_URL;

/**
 * Created by Rajendra Singh on 15/5/17.
 */

public class RecipeDataManager {
    private Realm realm;

    private static final String RECIPE_ID = "_id";
    private static final String COMMENT_ID = "_id";

    public RecipeDataManager() {
        realm = Realm.getDefaultInstance();
    }

    public List<RecipeRealmObject> fetchRecipe(ApiConstants.BadgeType badgeType) {
        RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class)
                .equalTo("badgeType", badgeType.value)
                .findAllSorted("badgeDateCreated", Sort.DESCENDING);
      /*  List<Recipe> recipeList = new ArrayList<>(recipeRealmObjectList.size());
        for (RecipeRealmObject recipeRealmObject : recipeRealmObjectList) {
            recipeList.add(getRecipe(recipeRealmObject));
        }
                return recipeRealmObjectList;
        */
        return realm.copyFromRealm(recipeRealmObjectList);
    }

    public Recipe getRecipe(RecipeRealmObject recipeRealmObject) {
        Recipe recipe = new Recipe();
        if (recipeRealmObject != null) {
            recipe.set_id(recipeRealmObject.get_id());
            recipe.setTitle(recipeRealmObject.getTitle());
            recipe.setType(recipeRealmObject.getType());
            if (recipeRealmObject.getCreatedBy() != null) {
                recipe.setCreatedById(recipeRealmObject.getCreatedBy().getId());
                recipe.setCreatedByName(recipeRealmObject.getCreatedBy().getName());
                recipe.setCreatedByProfileImageId(recipeRealmObject.getCreatedBy().getProfileImageId());
                recipe.setCreatedByFriendlyUrl(recipeRealmObject.getCreatedBy().getFriendlyUrl());
            }
            recipe.setCoverImage(recipeRealmObject.getCoverImage());
            if (recipeRealmObject.getMainImage() != null) {
                recipe.setMainImagePublicId(recipeRealmObject.getMainImage().getPublicId());
            }
            recipe.setFavorite(recipeRealmObject.isFavorite());
            if (recipeRealmObject.getCounter() != null) {
                recipe.setLikes(String.valueOf(recipeRealmObject.getCounter().getLikes()));
            }
            if (recipeRealmObject.getRating() != null) {
                recipe.setRatingAverage(recipeRealmObject.getRating().getAverage());

            }
            recipe.setBadgeDateCreated(recipeRealmObject.getBadgeDateCreated());
            if (recipeRealmObject.getCounter() != null) {
                recipe.setComments(recipeRealmObject.getCounter().getComments());
            }
            recipe.setBadgeType(recipeRealmObject.getBadgeType());
            recipe.setLastUpdated(recipeRealmObject.getLastUpdated());
        }
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

    public void updateIsFavoriteInDB(final boolean checked, final RecipeRealmObject recipe) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo(RECIPE_ID, recipe.get_id()).findFirst();
                recipeRealmObject.setFavorite(checked);
            }
        });
    }


    public void updateLikesCount(final RecipeRealmObject recipe, final long likes) {
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


    public void insertOrUpdateRecipe(JSONArray jsonArray) {
        JSONObjectUtility jsonUtility = new JSONObjectUtility();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                jsonObject = jsonUtility.changeKeyOfJSON(jsonObject, "name", "title");
                realm.beginTransaction();
                realm.createOrUpdateObjectFromJson(RecipeRealmObject.class, jsonObject);
                realm.commitTransaction();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public RecipeRealmObject fetchRecipe(String recipeID) {
        //return the managed object
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo(RECIPE_ID, recipeID).findFirst();
        return recipeRealmObject;
    }

    public RecipeRealmObject fetchRecipeUsingFriendlyUrl(String friendlyUrl) {
        //return the managed object
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo(FRIENDLY_URL, friendlyUrl).findFirst();
        return recipeRealmObject;
    }

    public RecipeRealmObject fetchCopyOfRecipe(String recipeID) {
        //        //return the unmanaged object
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo(RECIPE_ID, recipeID).findFirst();
        return realm.copyFromRealm(recipeRealmObject);
    }

    public void updateSimilarRecipe(final String friendlyUrl, final JSONArray jsonArray) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo(FRIENDLY_URL, friendlyUrl).findFirst();
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
                RealmList<CommentRealmObject> commentRealmObjectsUpdated = new RealmList<>();
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
                for (int i = 0; i < jsonArray.length(); i++) {
                    boolean isFound = false;
                    for (int j = 0; j < recipeRealmObject.getComments().size(); j++) {
                        if (commentRealmObjects.get(i).get_id().equals(recipeRealmObject.getComments().get(j).get_id())) {
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound) {
                        commentRealmObjectsUpdated.add(commentRealmObjects.get(i));
                    }
                }
                recipeRealmObject.getComments().addAll(commentRealmObjectsUpdated);
            }
        });
    }

    public void updateRandomCommentsList(final JSONArray jsonArray) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(CommentRealmObject.class, jsonArray);
            }
        });
    }

    public void insertCommentRealmObject(final String recipeID, final JSONObject commentObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                        .equalTo(RECIPE_ID, recipeID).findFirst();
                long commentCount = recipeRealmObject.getCounter().getComments() + 1;
                recipeRealmObject.getCounter().setComments(commentCount);
                CommentRealmObject commentRealmObject = realm.createOrUpdateObjectFromJson(CommentRealmObject.class, commentObject);
                recipeRealmObject.getComments().add(0, commentRealmObject);
            }
        });
    }

    public void insertCommentReplyEvents(final String commentId, final JSONObject commentObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // parent comment object
                CommentRealmObject commentRealmObject = realm.where(CommentRealmObject.class)
                        .equalTo(COMMENT_ID, commentId).findFirst();
                int replyCount = commentRealmObject.getPayload().getReplyCount() + 1;
                commentRealmObject.getPayload().setReplyCount(replyCount);
                // replied comment object
                CommentRealmObject replyCommentRealmObject = realm.createOrUpdateObjectFromJson(CommentRealmObject.class, commentObject);
                commentRealmObject.getPayload().getReplyEvents().add(replyCommentRealmObject);
            }
        });
    }

    public void updateCommentRealmObject(final JSONObject commentObject) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CommentRealmObject commentRealmObject = realm.createOrUpdateObjectFromJson(CommentRealmObject.class, commentObject);
                Logger.i("comment Name->", commentRealmObject.getName());
            }
        });
    }

    public RecipeRealmObject fetchCopyOfRecipeByFriendlyUrl(String friendlyUrl) {
        //        //return the unmanaged object
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class)
                .equalTo(FRIENDLY_URL, friendlyUrl).findFirst();
        return realm.copyFromRealm(recipeRealmObject);
    }

    public CommentRealmObject fetchCopyOfComment(String commentId) {
        //        //return the unmanaged object
        CommentRealmObject commentRealmObject = realm.where(CommentRealmObject.class)
                .equalTo(RECIPE_ID, commentId).findFirst();
        return realm.copyFromRealm(commentRealmObject);
    }


    public List<CommentRealmObject> fetchRandomCommentList() {
        RealmResults<CommentRealmObject> commentRealmObjectList = realm.where(CommentRealmObject.class)
                .findAllSorted("dateCreated", Sort.DESCENDING);
        List<CommentRealmObject> commentList = new ArrayList<>(commentRealmObjectList.size());
        for (CommentRealmObject commentRealmObject : commentRealmObjectList) {
            commentList.add(commentRealmObject);
        }
        return commentList;
    }

    public void removeRecipe(String friendlyUrl) {
        RecipeRealmObject recipeRealmObject = realm.where(RecipeRealmObject.class).equalTo("friendlyUrl", friendlyUrl).findFirst();
        if (recipeRealmObject != null) {
            realm.beginTransaction();
            Logger.e("Recipe Deleted", recipeRealmObject.getFriendlyUrl() + recipeRealmObject.getTitle());
            recipeRealmObject.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public void removeComment(String _id) {
        CommentRealmObject commentRealmObject = realm.where(CommentRealmObject.class).equalTo("_id", _id).findFirst();
        if (commentRealmObject != null) {
            realm.beginTransaction();
            Logger.e("Comment Deleted", commentRealmObject.get_id());
            commentRealmObject.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}



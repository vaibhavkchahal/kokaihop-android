package com.kokaihop.userprofile;

import com.google.gson.Gson;
import com.kokaihop.database.CookbookRealmObject;
import com.kokaihop.database.RealmString;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.feed.Recipe;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.FollowingFollowerUser;
import com.kokaihop.userprofile.model.Settings;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.userprofile.model.UserName;
import com.kokaihop.utility.JSONObjectUtility;
import com.kokaihop.utility.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class ProfileDataManager {
    private Realm realm;
    private Gson gson;

    //    Default Constructor
    public ProfileDataManager() {
        realm = Realm.getDefaultInstance();
        gson = new Gson();
    }

    private UserRealmObject getUser(String key, String value) {
        return realm.where(UserRealmObject.class).equalTo(key, value).findFirst();
    }

    //    Geting the user data from the database on the basis of userId
    public User fetchUserData(String userId, User user) {
        UserRealmObject userRealmObject = getUser("_id", userId);
        return getUserData(userRealmObject, user);
    }

    //    Setting user data obtained from database to a User Model object
    private User getUserData(UserRealmObject userRealmObject, User user) {
        if (userRealmObject != null) {
            user.set_id(userRealmObject.getId());
            user.setFriendlyUrl(userRealmObject.getFriendlyUrl());
            user.setName(new UserName());
            if (userRealmObject.getUserNameRealmObject() != null) {
                user.getName().setFirst(userRealmObject.getUserNameRealmObject().getFirst());
                user.getName().setLast(userRealmObject.getUserNameRealmObject().getLast());
                user.getName().setFull(userRealmObject.getUserNameRealmObject().getFull());
            }
            user.setEmail(userRealmObject.getEmail());
            user.getFollowing().clear();
            for (RealmString userid : userRealmObject.getFollowing()) {
                user.getFollowing().add(userid.getUserId());
            }
            user.setFollowByMe(User.getInstance().getFollowing().contains(user.get_id()));
            user.getFollowers().clear();
            for (RealmString userid : userRealmObject.getFollowers()) {
                user.getFollowers().add(userid.getUserId());
            }
            if (userRealmObject.getProfileImage() != null) {
                user.setProfileImage(new CloudinaryImage());
                user.getProfileImage().setCloudinaryId(userRealmObject.getProfileImage().getCloudinaryId());
            }
            if (userRealmObject.getCoverImage() != null) {
                user.setCoverImage(new CloudinaryImage());
                user.getCoverImage().setCloudinaryId(userRealmObject.getCoverImage().getCloudinaryId());
            }
            user.setRecipeCount(userRealmObject.getRecipeCount());
            user.setRecipesCollectionCount(userRealmObject.getRecipesCollectionCount());
            user.setSettings(new Settings());
            if (userRealmObject.getSettingsRealmObject() != null) {
                user.getSettings().setNewsletters(userRealmObject.getSettingsRealmObject().isNewsletters());
                user.getSettings().setSuggestionsOfTheDay(userRealmObject.getSettingsRealmObject().isSuggestionsOfTheDay());
                user.getSettings().setNoEmails(userRealmObject.getSettingsRealmObject().isNoEmails());

            }
            ArrayList<Cookbook> cookbooks = new ArrayList<>();
            for (CookbookRealmObject cookbookRealmObject : userRealmObject.getRecipeCollections()) {
                Cookbook cookbook = new Cookbook();
                cookbook.set_id(cookbookRealmObject.get_id());
                cookbook.setName(cookbookRealmObject.getName());
                cookbook.setFriendlyUrl(cookbookRealmObject.getFriendlyUrl());
            }
            user.setCookbooks(cookbooks);
            user.setCityName(userRealmObject.getCityName());
        }
        return user;
    }

    //    Inserting or updating the user data into the database
    public void insertOrUpdateUserData(UserRealmObject userRealmObject) {
        realm.beginTransaction();
        String json = gson.toJson(userRealmObject);
        realm.createOrUpdateObjectFromJson(UserRealmObject.class, json);
        realm.commitTransaction();
    }

    public void insertOrUpdateUserDataUsingJSON(JSONObject jsonObject) {
        JSONObjectUtility jsonUtility = new JSONObjectUtility();
        jsonObject = jsonUtility.changeKeyOfJSON(jsonObject, "_id", "id");
        jsonObject = jsonUtility.convertStringArrayToRealmStringArray(jsonObject, "followers");
        jsonObject = jsonUtility.convertStringArrayToRealmStringArray(jsonObject, "following");
//        jsonObject.remove("followers");
//        jsonObject.remove("following");
        realm.beginTransaction();
        UserRealmObject obj = realm.createOrUpdateObjectFromJson(UserRealmObject.class, jsonObject);
        realm.commitTransaction();
        Logger.e("Value Inserted", "CollectionCount = " + obj.getRecipesCollectionCount());
    }

    public void insertOrUpdateCookbooksUsingJSON(JSONArray cookbooks, String userId) {
        UserRealmObject userRealmObject = getUser("_id", userId);
        if (userRealmObject != null) {
            for (int i = 0; i < cookbooks.length(); i++) {
                try {
                    JSONObject jsonObject = cookbooks.getJSONObject(i);
                    CookbookRealmObject cookbook = new CookbookRealmObject();
                    cookbook.set_id(jsonObject.getString("_id"));
                    cookbook.setName(jsonObject.getString("name"));
                    cookbook.setTotalCount(jsonObject.getInt("totalCount"));
                    cookbook.setFriendlyUrl(jsonObject.getString("friendlyUrl"));
                    JSONArray recipes = jsonObject.getJSONArray("recipes");
                    RealmList<RecipeRealmObject> recipesList = new RealmList<>();
                    for (int j = 0; j < recipes.length(); j++) {
                        RecipeRealmObject recipe = new RecipeRealmObject();
                        JSONObject recipeJson = recipes.getJSONObject(j);
                        try {
                            recipe.setFriendlyUrl(recipeJson.getString("friendlyUrl"));
                            recipe.set_id(recipeJson.getString("id"));
                            recipe.setTitle(recipeJson.getString("name"));
                            recipe.setCategoryImagePublicId(recipeJson.getString("categoryImagePublicId"));
                            recipe.setCoverImage(recipeJson.getString("coverImage"));
                            recipesList.add(recipe);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    realm.beginTransaction();
                    cookbook.setRecipes(recipesList);
                    realm.createOrUpdateObjectFromJson(CookbookRealmObject.class, jsonObject);
                    if (!cookbookAlreadyExists(userRealmObject.getRecipeCollections(), cookbook)) {
                        userRealmObject.getRecipeCollections().add(cookbook);
                    }
                    realm.commitTransaction();
//                    new RecipeDataManager().insertOrUpdateRecipe(recipes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


    }


    //    Inserting or updating the following users data into the database
    public void insertOrUpdateFollowing(final RealmList<UserRealmObject> userRealmObjectList, final String userId) {

        UserRealmObject userRealmObject = getUser("_id", userId);

        if (userRealmObject != null) {
            realm.beginTransaction();
            for (UserRealmObject following : userRealmObjectList) {
                if (!following.getId().equals(userId)) {
                    realm.insertOrUpdate(following);
                    if (!userAlreadyExists(userRealmObject.getFollowingList(), following)) {
                        userRealmObject.getFollowingList().add(following);
                    }
                }
            }
            realm.commitTransaction();
        }

    }

    //    Inserting or updating the followers data into the database
    public void insertOrUpdateFollowers(RealmList<UserRealmObject> userRealmObjectList, String userId) {
        final UserRealmObject userRealmObject = getUser("_id", userId);
        while (realm.isInTransaction()) ;
        realm.beginTransaction();
        if (userRealmObject != null) {
            for (UserRealmObject follower : userRealmObjectList) {
                if (!follower.getId().equals(userId)) {
                    realm.insertOrUpdate(follower);
                    if (!userAlreadyExists(userRealmObject.getFollowersList(), follower)) {
                        userRealmObject.getFollowersList().add(follower);
                    }
                }
            }
        }
        realm.commitTransaction();
    }

    //Getting the  user
    public ArrayList<FollowingFollowerUser> fetchFollowersList(String userId) {
        ArrayList<FollowingFollowerUser> followersList = new ArrayList<>();
        UserRealmObject userRealmObject = getUser("_id", userId);
        if (userRealmObject != null) {
            RealmList<UserRealmObject> userRealmObjects = userRealmObject.getFollowersList();
            for (UserRealmObject follower : userRealmObjects) {
                FollowingFollowerUser user = new FollowingFollowerUser();
                user.set_id(follower.getId());
                user.setFriendlyUrl(follower.getFriendlyUrl());
                user.setName(new UserName());
                user.getName().setFull(follower.getUserNameRealmObject().getFull());
                if (follower.getProfileImage() != null) {
                    user.setProfileImage(new CloudinaryImage());
                    user.getProfileImage().setCloudinaryId(follower.getProfileImage().getCloudinaryId());
                }
                followersList.add(user);
            }
        }
        return followersList;
    }

    public ArrayList<FollowingFollowerUser> fetchFollowingList(String userId) {
        ArrayList<FollowingFollowerUser> followingList = new ArrayList<>();

        UserRealmObject userRealmObject = getUser("_id", userId);
        RealmList<UserRealmObject> userRealmObjects;
        if (userRealmObject != null) {
            userRealmObjects = userRealmObject.getFollowingList();
            Logger.e("FollowingDB", userRealmObjects.size() + "");
            for (UserRealmObject following : userRealmObjects) {
                FollowingFollowerUser user = new FollowingFollowerUser();
                user.set_id(following.getId());
                user.setName(new UserName());
                user.getName().setFull(following.getUserNameRealmObject().getFull());
                if (following.getProfileImage() != null) {
                    user.setProfileImage(new CloudinaryImage());
                    user.getProfileImage().setCloudinaryId(following.getProfileImage().getCloudinaryId());
                }
                followingList.add(user);
            }
        }
        return followingList;
    }

    public void removeData() {
        realm.beginTransaction();
        realm.where(UserRealmObject.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    //To check whether the user already exists in list or not
    public boolean userAlreadyExists(RealmList<UserRealmObject> list, UserRealmObject user) {
        for (UserRealmObject userInList : list) {
            if (user.getId().equals(userInList.getId()))
                return true;
        }
        return false;
    }

    public boolean cookbookAlreadyExists(RealmList<CookbookRealmObject> list, CookbookRealmObject cookbookRealmObject) {
        for (CookbookRealmObject cookbook : list) {
            if (cookbookRealmObject.get_id().equals(cookbook.get_id()))
                return true;
        }
        return false;
    }

    public boolean recipeAlreadyExists(RealmList<RecipeRealmObject> list, RecipeRealmObject recipeRealmObject) {
        for (RecipeRealmObject userInList : list) {
            if (recipeRealmObject.get_id().equals(userInList.get_id()))
                return true;
        }
        return false;
    }

    /*
    Clear the favorite preference of the user in recipes
     */
    public void updateIsFavoriteForAllRecipe() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class).equalTo("isFavorite", true)
                        .findAll();
                for (RecipeRealmObject recipeRealmObject : recipeRealmObjectList
                        ) {
                    recipeRealmObject.setFavorite(false);

                }
            }
        });
    }

    public void updateLastUpdatedTimeForAllRecipe() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RecipeRealmObject> recipeRealmObjectList = realm.where(RecipeRealmObject.class).greaterThanOrEqualTo("lastUpdated", 1)
                        .findAll();
                for (RecipeRealmObject recipeRealmObject : recipeRealmObjectList
                        ) {
                    recipeRealmObject.setLastUpdated(0);

                }
            }
        });
    }


    //    inserts the recipe list into the database
    public void insertOrUpdateRecipeObjects(final JSONArray recipes, String userId) {
        final UserRealmObject userRealmObject = getUser("_id", userId);
        if (userRealmObject != null) {
            realm.beginTransaction();
            gson = new Gson();
            for (int i = 0; i < recipes.length(); i++) {
                try {
                    JSONObjectUtility jsonObjectUtility = new JSONObjectUtility();
                    JSONObject jsonObject = recipes.getJSONObject(i);
                    jsonObject = jsonObjectUtility.updateCookingStepsInRecipe(jsonObject);
                    jsonObject = jsonObjectUtility.removeKeyFromJSON(jsonObject, "similarRecipes");
//                    realm.createOrUpdateObjectFromJson(RecipeRealmObject.class, jsonObject);
                    RecipeRealmObject recipeRealmObject = gson.fromJson(jsonObject.toString(), RecipeRealmObject.class);
                    if (!recipeAlreadyExists(userRealmObject.getRecipeList(), recipeRealmObject)) {
                        userRealmObject.getRecipeList().add(recipeRealmObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            realm.commitTransaction();
        }
    }

    //    returns the list of recipes created by the user(for whome the user ID is provided)
    public ArrayList<Recipe> getRecipesOfUser(String userId) {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        RealmList<RecipeRealmObject> recipeRealmObjects = new RealmList<>();
        UserRealmObject userRealmObject = getUser("_id", userId);
        if (userRealmObject != null) {
            recipeRealmObjects = userRealmObject.getRecipeList();
        }
        for (RecipeRealmObject realmObject : recipeRealmObjects) {
            Recipe recipe = new Recipe();
            recipe.set_id(realmObject.get_id());
            recipe.setTitle(realmObject.getTitle());
            if (realmObject.getCreatedBy() != null) {
                recipe.setCreatedByName(realmObject.getCreatedBy().getName());
            }
            if (realmObject.getRating() != null) {
                recipe.setRatingAverage(realmObject.getRating().getAverage());
            }
            if (realmObject.getMainImage() != null) {
                recipe.setMainImagePublicId(realmObject.getMainImage().getPublicId());
            }
            recipeList.add(recipe);
        }
        return recipeList;
    }

    public ArrayList<Cookbook> getCookbooks(String userId) {
        ArrayList<Cookbook> cookbooks = new ArrayList<>();
        RealmList<CookbookRealmObject> cookbookRealmList;
        UserRealmObject userRealmObject = getUser("_id", userId);
        if (userRealmObject != null) {
            cookbookRealmList = userRealmObject.getRecipeCollections();
            for (CookbookRealmObject realmObject : cookbookRealmList) {
                Cookbook cookbook = new Cookbook();
                cookbook.set_id(realmObject.get_id());
                cookbook.setFriendlyUrl(realmObject.getFriendlyUrl());
                cookbook.setName(realmObject.getName());
                cookbook.setTotal(realmObject.getTotalCount());
                if (realmObject.getRecipes().size() > 0) {
                    RecipeRealmObject recipe = realmObject.getRecipes().get(0);
                    if (recipe.getMainImage() != null) {
                        cookbook.setMainImageUrl(recipe.getMainImage().getPublicId());
                    } else {
                        cookbook.setMainImageUrl(recipe.getCoverImage());
                    }
                }
                cookbooks.add(cookbook);
            }
        }
        return cookbooks;
    }

    public String getFriendlyUrlOfUser(String userId) {
        String friendlyUrl = "";
        UserRealmObject userRealmObject = getUser("_id", userId);
        if (userRealmObject != null) {
            friendlyUrl = userRealmObject.getFriendlyUrl();
        }
        return friendlyUrl;
    }
}

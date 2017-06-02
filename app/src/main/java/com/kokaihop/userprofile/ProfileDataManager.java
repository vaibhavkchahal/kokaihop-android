package com.kokaihop.userprofile;

import com.kokaihop.database.StringObject;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.FollowingFollowerUser;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.userprofile.model.UserName;
import com.kokaihop.utility.Logger;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class ProfileDataManager {
    private Realm realm;

    public ProfileDataManager() {
        realm = Realm.getDefaultInstance();
    }

    public User fetchUserData(String userId) {
        UserRealmObject userRealmObject = realm.where(UserRealmObject.class).equalTo("id",userId).findFirst();
        User user = getUserData(userRealmObject);
        return user;
    }

    private User getUserData(UserRealmObject userRealmObject) {
        User user = User.getInstance();
        if (userRealmObject != null) {
            user.set_id(userRealmObject.getId());
            user.setName(new UserName());
            user.getName().setFirst(userRealmObject.getUserNameRealmObject().getFirst());
            user.getName().setLast(userRealmObject.getUserNameRealmObject().getLast());
            user.getName().setFull(userRealmObject.getUserNameRealmObject().getFull());
            user.getFollowing().clear();
            for (StringObject userid : userRealmObject.getFollowing()) {
                user.getFollowing().add(userid.getString());
            }

            user.getFollowers().clear();
            for (StringObject userid : userRealmObject.getFollowers()) {
                user.getFollowers().add(userid.getString());
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
        }

        return user;
    }

    public void insertOrUpdateUserData(UserRealmObject userRealmObject) {
        realm.beginTransaction();
        realm.insertOrUpdate(userRealmObject);
        realm.commitTransaction();
    }

    public void insertOrUpdateFollowing(final RealmList<UserRealmObject> userRealmObjectList, final String userId) {

        UserRealmObject userRealmObject = realm.where(UserRealmObject.class).equalTo("id", userId).findFirst();

        realm.beginTransaction();

        for (UserRealmObject following : userRealmObjectList) {

            if (!following.getId().equals(userId)) {
                realm.insertOrUpdate(following);

                if (!alreadyExists(userRealmObject.getFollowingList(), following)) {
                    userRealmObject.getFollowingList().add(following);
                } else {
                    Logger.e("Exists Following", following.getUserNameRealmObject().getFull());
                }
            }
        }
        realm.commitTransaction();

    }

    public void insertOrUpdateFollowers(RealmList<UserRealmObject> userRealmObjectList, String userId) {
        final UserRealmObject userRealmObject = realm.where(UserRealmObject.class).equalTo("id", userId).findFirst();
        realm.beginTransaction();

        for (UserRealmObject follower : userRealmObjectList) {

            if (!follower.getId().equals(userId)) {
                realm.insertOrUpdate(follower);

                if (!alreadyExists(userRealmObject.getFollowersList(), follower)) {
                    userRealmObject.getFollowersList().add(follower);
                } else {
                    Logger.e("Exists Follower", follower.getUserNameRealmObject().getFull());
                }
            }
        }
        realm.commitTransaction();
    }

    public ArrayList<FollowingFollowerUser> fetchFollowersList(String userId) {
        ArrayList<FollowingFollowerUser> followersList = new ArrayList<>();
        RealmList<UserRealmObject> userRealmObjects = realm.where(UserRealmObject.class).equalTo("id", userId).findFirst().getFollowersList();
        for (UserRealmObject follower : userRealmObjects) {
            FollowingFollowerUser user = new FollowingFollowerUser();
            user.set_id(follower.getId());
            user.setName(new UserName());
            user.getName().setFull(follower.getUserNameRealmObject().getFull());
            if (follower.getProfileImage() != null) {
                user.setProfileImage(new CloudinaryImage());
                user.getProfileImage().setCloudinaryId(follower.getProfileImage().getCloudinaryId());
            }

            followersList.add(user);
        }
        return followersList;
    }
    public ArrayList<FollowingFollowerUser> fetchFollowingList(String userId) {
        ArrayList<FollowingFollowerUser> followingList = new ArrayList<>();

        UserRealmObject userRealmObject = realm.where(UserRealmObject.class).equalTo("id", userId).findFirst();
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
                Logger.e("Following User", user.getName().getFull());

                followingList.add(user);
            }
        }
        return followingList;
    }

    public void removeData(String userId) {
        realm.beginTransaction();
        realm.where(UserRealmObject.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    //To check whether the user already exists in list or not
    public boolean alreadyExists(RealmList<UserRealmObject> list, UserRealmObject user) {
        for (UserRealmObject userInList : list) {
            if (user.getId().equals(userInList.getId()))
                return true;
        }
        return false;
    }
}

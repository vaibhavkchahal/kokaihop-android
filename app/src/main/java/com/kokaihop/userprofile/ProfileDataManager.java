package com.kokaihop.userprofile;

import com.kokaihop.database.StringObject;
import com.kokaihop.database.UserRealmObject;
import com.kokaihop.userprofile.model.CloudinaryImage;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.userprofile.model.UserName;
import com.kokaihop.utility.Logger;

import io.realm.Realm;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class ProfileDataManager {
    private Realm realm;

    public ProfileDataManager() {
        realm = Realm.getDefaultInstance();
    }

    public User fetchUserData() {
        UserRealmObject userRealmObject = realm.where(UserRealmObject.class).findFirst();
        User user = getUserData(userRealmObject);
        return user;
    }

    private User getUserData(UserRealmObject userRealmObject) {
        User user = User.getInstance();
        user.set_id(userRealmObject.get_id());
        user.setName(new UserName());
        user.getName().setFirst(userRealmObject.getName().getFirst());
        user.getName().setLast(userRealmObject.getName().getLast());
        user.getName().setFull(userRealmObject.getName().getFull());

        for(StringObject userid : userRealmObject.getFollowing()){
            user.getFollowing().add(userid.getString());
        }
        for(StringObject userid : userRealmObject.getFollowers()){
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
        return user;
    }

    public void insertOrUpdate(UserRealmObject userRealmObject) {
        realm.beginTransaction();
        realm.insertOrUpdate(userRealmObject);
        realm.commitTransaction();
    }
}

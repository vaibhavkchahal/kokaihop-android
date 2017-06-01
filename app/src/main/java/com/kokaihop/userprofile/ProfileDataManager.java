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
        UserRealmObject userRealmObject = realm.where(UserRealmObject.class).equalTo("_id", userId).findFirst();
        User user = getUserData(userRealmObject);
        return user;
    }

    private User getUserData(UserRealmObject userRealmObject) {
        User user = User.getInstance();
        if(userRealmObject!=null){
            user.set_id(userRealmObject.get_id());
            user.setName(new UserName());
            user.getName().setFirst(userRealmObject.getName().getFirst());
            user.getName().setLast(userRealmObject.getName().getLast());
            user.getName().setFull(userRealmObject.getName().getFull());
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

    public void insertOrUpdate(UserRealmObject userRealmObject) {
        realm.beginTransaction();
        realm.insertOrUpdate(userRealmObject);
        realm.commitTransaction();
    }

    public void insertOrUpdateFollowing(RealmList<UserRealmObject> userRealmObjectRealmList, final String userId) {

        UserRealmObject userRealmObject = realm.where(UserRealmObject.class).equalTo("_id", userId).findFirst();
        realm.beginTransaction();
        Logger.e("Following ", userRealmObject.getFollowingList().size() + "");

        for (UserRealmObject following : userRealmObjectRealmList) {
            if(!following.get_id().equals(userId)){
                realm.insertOrUpdate(following);
                userRealmObject.getFollowingList().add(following);
            }
        }
        Logger.e("Following ", userRealmObject.getFollowingList().size() + "");
        realm.commitTransaction();

//        realm.where(UserRealmObject.class).equalTo("_id",userId).findFirst().setFollowingList(followingList);

//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                UserRealmObject userRealmObject = realm.where(UserRealmObject.class)
//                        .equalTo("_id", userId).findFirst();
//                userRealmObject.setFollowingList(followingList);
//            }
//        });

    }

    public void insertOrUpdateFollowers(RealmList<UserRealmObject> userRealmObjectRealmList, String userId) {
        UserRealmObject userRealmObject = realm.where(UserRealmObject.class).equalTo("_id", userId).findFirst();
        realm.beginTransaction();
        for (UserRealmObject follower : userRealmObjectRealmList) {
            realm.insertOrUpdate(follower);
            userRealmObject.getFollowersList().add(follower);
        }
        realm.commitTransaction();
    }

    public ArrayList<FollowingFollowerUser> fetchFollowersList(String userId) {
        ArrayList<FollowingFollowerUser> followersList = new ArrayList<>();
        RealmList<UserRealmObject> userRealmObjects = realm.where(UserRealmObject.class).equalTo("_id", userId).findFirst().getFollowersList();
        for (UserRealmObject follower : userRealmObjects) {
            FollowingFollowerUser user = new FollowingFollowerUser();
            user.set_id(follower.get_id());
            user.setName(new UserName());
            user.getName().setFull(follower.getName().getFull());
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
        UserRealmObject userRealmObject =  realm.where(UserRealmObject.class).equalTo("_id", userId).findFirst();
        RealmList<UserRealmObject> userRealmObjects = new RealmList<>();
        if(userRealmObject!=null){
            userRealmObjects = userRealmObject.getFollowingList();
            for (UserRealmObject following : userRealmObjects) {
                FollowingFollowerUser user = new FollowingFollowerUser();
                user.set_id(following.get_id());
                user.setName(new UserName());
                user.getName().setFull(following.getName().getFull());
                if (following.getProfileImage() != null) {
                    user.setProfileImage(new CloudinaryImage());
                    user.getProfileImage().setCloudinaryId(following.getProfileImage().getCloudinaryId());
                }
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
}

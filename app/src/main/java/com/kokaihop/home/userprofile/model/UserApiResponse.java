package com.kokaihop.home.userprofile.model;

import android.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 17/5/17.
 */

public class UserApiResponse extends BaseObservable{

    @SerializedName("_id")
    private String _id;
    @SerializedName("friendlyUrl")
    private String friendlyUrl;
    @SerializedName("oldId")
    private int oldId;
    @SerializedName("email")
    private String email;
    @SerializedName("location")
    private Location location;
    @SerializedName("followers")
    private String[] followers;
    @SerializedName("following")
    private String[] following;
    @SerializedName("dateCreated")
    private long dateCreated;
    @SerializedName("loginCount")
    private int loginCount;
    @SerializedName("lastLoginDate")
    private long lastLoginDate;
    @SerializedName("settings")
    private Settings settings;

}
{
        "_id": "56387ade1a258f0300c3074e",
        "friendlyUrl": "roni-thomas",
        "oldId": 13,
        "email": "roni@intelligrape.com",
        "location": {
        "current": {
        "loc": {
        "coordinates": [],
        "type": "Point"
        }
        },
        "living": {
        "loc": {
        "coordinates": [],
        "type": "Point"
        }
        }
        },
        "followers": [
        "56387a161e443c0300bfa415",
        "56387a791e443c0300c3a7ad"
        ],
        "following": [
        "56387ade1a258f0300c3074f",
        "56387aa11e443c0300c57001",
        "56387aa11e443c0300c5696f",
        "56387a291e443c0300c05541",
        "563879d51e443c0300bd0bf7",
        "56387aa81e443c0300c5b186",
        "56387aa41e443c0300c57acb"
        ],
        "dateCreated": 1446542046150,
        "loginCount": 1,
        "lastLoginDate": 1495095161084,
        "settings": {
        "blogYouCommented": {
        "email": "never",
        "mobile": "never"
        },
        "recipeYouCommented": {
        "email": "never",
        "mobile": "never"
        },
        "blogComment": {
        "mobile": "instant",
        "email": "daily"
        },
        "newPrescriptionImage": {
        "email": "never",
        "mobile": "never"
        },
        "recipeComment": {
        "mobile": "instant",
        "email": "instant"
        },
        "reviewedRecipe": {
        "email": "never",
        "mobile": "never"
        },
        "newFollower": {
        "email": "never",
        "mobile": "never"
        },
        "newMessage": {
        "email": "never",
        "mobile": "never"
        },
        "newsletters": true,
        "suggestionsOfTheDay": true,
        "notificationSoundEnabled": true,
        "web": {
        "profilePicture": true,
        "newRecipes": false,
        "newBlogs": false
        },
        "noEmails": false
        },
        "lastViewedNewsOn": 1495108764713,
        "role": "ADMIN",
        "enabled": true,
        "name": {
        "first": "Roni",
        "last": "C. Thomas",
        "full": "Roni C. Thomas"
        },
        "__v": 0,
        "profileImage": {
        "cloudinaryId": "ow2ldxtqkppxi4lak6pb",
        "uploaded": 1493353400620
        },
        "lastSeenTime": 1478670696856,
        "isOnline": false,
        "aboutMe": "jhbsa ajhvasuy shvs uhasdhuuyd hud uyasd asdjh Allas favoritsmaker från tacos i en smakrik sallad med kyckling och krämig dressing på gräddfil och ko. Allas favoritsmaker från tacos i en smakrik sallad med kyckling och krämig dressing på gräddfil och ko. Allas favoritsmaker från tacos i en smakrik sallad med kyckling och krämig dressing på gräddfil och ko. Allas favoritsmaker från tacos i en smakrik sallad med kyckling och krämig dressing på gräddfil och ko. Allas favoritsmaker från tacos i en smakrik sallad med kyckling och krämig dressing på gräddfil och ko.",
        "counter": {
        "questions": 7,
        "answers": 16
        },
        "hasPassword": true,
        "recipesCollectionCount": 11,
        "blogPostCount": 15,
        "coverImage": {
        "cloudinaryId": "3489849"
        },
        "recipeCount": 221,
        "totalFeeds": 228
        }

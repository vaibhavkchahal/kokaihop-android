package com.kokaihop.utility;

/**
 * Created by Vaibhav Chahal on 8/5/17.
 */

public class Constants {

    public static final String APP_NAME = "Kokaihop";
    public static final int PASSWORD_MIN_LENGTH = 7;
    public static final long SPLASH_VISIBLE_TIME = 3000;
    public static final String SHARED_PREFERENCES = "MyPrefs";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String AUTHORIZATION_BEARER = "Bearer ";
    public static final String USER_ID = "userId";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_FRIENDLY_URL = "courseFriendlyUrl";
    public static final String CUISINE_FRIENDLY_URL = "cuisineFriendlyUrl";
    public static final String CUISINE_NAME = "cuisineName";
    public static final String METHOD_NAME = "methodName";
    public static final String METHOD_FRIENDLY_URL = "methodFriendlyUrl";

    public static final String FRIENDLY_URL = "friendlyUrl";
    public static final String RECIPE_POSITION = "recipePosition";
    public static final String LOGIN_TYPE = "loginType";
    public static final String FACEBOOK_LOGIN = "facebook";
    public static final String EMAIL_LOGIN = "email";

    public static final String SHOPPING_LIST_NAME_KEY = "name";

    public static final String SHOPPING_LIST_NAME_VALUE = "Min inkopslista";
    public static final String SHOPPING_LIST_DEFAULT_FRIENDLY_URL = "Min inkopslista";

    //Tab Numbers
    public static final int TAB_RECIPES = 0;
    public static final int TAB_FOLLOWERS = 1;
    public static final int TAB_FOLLOWINGS = 2;
    public static final int TAB_HISTORY = 3;

    public static final int TAB_OTHER_RECIPES = 0;
    public static final int TAB_OTHER_COOKBOOKS = 1;
    public static final int TAB_OTHER_FOLLOWERS = 2;
    public static final int TAB_OTHER_FOLLOWINGS = 3;
    public static final String EXTRA_FROM = "extraFrom";

    // for cloudinary request
    public static String REQUEST_KEY_CLOUDINARY_API_KEY = "api_key";
    public static String REQUEST_KEY_CLOUDINARY_API_SECRET = "api_secret";
    public static String REQUEST_KEY_CLOUDINARY_FOLDER = "folder";
    public static String REQUEST_KEY_CLOUDINARY_CLOUD_NAME = "cloud_name";
    public static String REQUEST_KEY_CLOUDINARY_FORMAT = "format";
    public static String REQUEST_KEY_CLOUDINARY_IMAGE_PATH = "imagePath";


    public static final String COUNTRY_CODE = "en";
    public static final String RECIPE_ID = "recipeId";
    public static final String COOKBOOK_FRIENDLY_URL = "cookbookFriendlyUrl";
    public static final String USER_FRIENDLY_URL = "userFriendlyUrl";
    public static final String COOKBOOK_TITLE = "cookbookTitle";
    public static final String FAVORITE_RECIPE_FRIENDLY_URL = "favoritrecept";
    public static final String COLLECTION_MAPPING = "collectionMappings";

    //    RequestCodes
    public static final int USERPROFILE_REQUEST = 4;
    public static final int COOKBOOK_REQUEST = 1;

    public static String USER_Email_PASSWORD = "Email_Pwd";
    public static int CONFIRM_REQUEST_CODE = 41;


    // shopping list request codes
    public static final int ADD_INGREDIENT_REQUEST_CODE = 300;
    public static final String INGREDIENT_NAME = "ingredientName";
    public static final String INGREDIENT_AMOUNT = "amount";
    public static final String INGREDIENT_UNIT = "unit";
    public static final String INGREDIENT_ID = "id";
    public static final String SHOW_DIALOG_ACTION = "showdialogAction";


    public static final String EDIT_INGGREDIENT_TAG = "100";
    public static final String MARKED_INGREDIENT_TAG = "200";

    public static final String TEMP_INGREDIENT_ID_SIGNATURE = "temp";


    // coach mark keys
    public static final String SEARCH_COACHMARK_VISIBILITY = "searchCoachMark";
    public static final String USERPROFILE_COACHMARK_VISIBILITY = "userprofileCoachMark";
    public static final String RECIPE_DETAIL_COACHMARK_VISIBILITY = "recipeDetailCoachMark";


    public static final String DEACTIVATED_RECIPES = "DEACTIVATED_RECIPES";
    public static final String DELETED_RECIPE_COLLECTIONS = "DELETED_RECIPE_COLLECTIONS";
    public static final String DELETED_RECIPE_COMMENTS = "DELETED_RECIPE_COMMENTS";

    public static final String RECIPE_TIME_STAMP = "RECIPE_TIME_STAMP";
    public static final String COMMENTS_TIME_STAMP = "COMMENTS_TIME_STAMP";
    public static final String COOKBOOK_TIME_STAMP = "COOKBOOK_TIME_STAMP";

    public static final int NUMBER_PICKER_HEIGHT = 1;

    // tablet support recipe columns constants on home
    public static final int TAB_10_INCH_FIRST_ADD_POSITION_PORT = 5;
    public static final int TAB_10_ADD_REPEAT_POSITION_PORT = 13;

    public static final int TAB_10_INCH_FIRST_ADD_POSITION_LAND = 6;
    public static final int TAB_10_ADD_REPEAT_POSITION_LAND = 16;

    public static final int TAB_7_INCH_FIRST_ADD_POSITION_PORT = 4;
    public static final int TAB_7_ADD_REPEAT_POSITION_PORT = 10;
    public static final int TAB_7_INCH_FIRST_ADD_POSITION_LAND = 6;
    public static final int TAB_7_ADD_REPEAT_POSITION_LAND = 16;
    public static final int PHONE_FIRST_ADD_POSITION = 3;
    public static final int PHONE_ADD_REPEAT_POSITION = 7;


    // recipe detail request code
    public static final int OPEN_USER_PROFILE_REQUEST_CODE = 500;
    public static final int OPEN_USER_PROFILE_RESULT_CODE = 600;

    public static final int BOTTOM_SHET_DIALOG_PEEK_HEIGHT = 1000;

}

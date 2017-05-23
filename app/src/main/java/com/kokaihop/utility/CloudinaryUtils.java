package com.kokaihop.utility;

import kokaihop.utility.CloudinaryDetail;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class CloudinaryUtils {

    public static final String FORMAT = ".jpg";
    public static final String SEPARATOR = "/";
    public static final String COMMA = ",";


    public static String getImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.url + CloudinaryDetail.cloudName + CloudinaryDetail.folder + "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + SEPARATOR + publicId + FORMAT;
        Logger.e("imageUrl", url);
        return url;
    }

    public static String getRoundedImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.url + CloudinaryDetail.cloudName + CloudinaryDetail.folder + "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + COMMA + "r_max" + COMMA + "g_face" + SEPARATOR + publicId + FORMAT;
        Logger.e("imageUrl", url);
        return url;
    }


}

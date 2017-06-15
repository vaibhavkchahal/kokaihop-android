package com.kokaihop.utility;


import com.cloudinary.Cloudinary;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.kokaihop.utility.Constants.REQUEST_KEY_CLOUDINARY_IMAGE_PATH;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class CloudinaryUtils {

    public static final String FORMAT = ".jpg";
    public static final String SEPARATOR = "/";
    public static final String COMMA = ",";


    public static String getImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.URL + SEPARATOR + CloudinaryDetail.CLOUD_NAME + SEPARATOR + CloudinaryDetail.FOLDER + SEPARATOR + "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + SEPARATOR + publicId + FORMAT;
//        Logger.e("imageUrl", URL);
        return url;
    }

    public static String getRoundedImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.URL + SEPARATOR + CloudinaryDetail.CLOUD_NAME + SEPARATOR + CloudinaryDetail.FOLDER + SEPARATOR + "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + COMMA + "r_max" + COMMA + "g_face" + SEPARATOR + publicId + FORMAT;
//        Logger.e("imageUrl", URL);
        return url;
    }

    public static String getRoundedCornerImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.URL + SEPARATOR + CloudinaryDetail.CLOUD_NAME + SEPARATOR + CloudinaryDetail.FOLDER + SEPARATOR + "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + COMMA + "r_50" + COMMA + "g_face" + SEPARATOR + publicId + FORMAT;
//        Logger.e("imageUrl", URL);
        return url;
    }



    public Map<String, String> uploadImageOnCloudinary(HashMap<String, String> config) {
        Cloudinary cloudinary = new Cloudinary(config);
        Map<String, String> uploadResult = null;
        String imagePath = config.get(REQUEST_KEY_CLOUDINARY_IMAGE_PATH);
        try {

            // Upload image from url e.g facebook, google+
            if (imagePath.startsWith("http")) {
                uploadResult = cloudinary.uploader().upload(imagePath, config);
            }
            // upload image from SD Card
            else {
                Logger.i("imagePath", imagePath + "");
                Logger.i("config", config.toString());
                File file = new File(imagePath);
                InputStream inputStream = new FileInputStream(file);
                uploadResult = cloudinary.uploader().upload(inputStream, config);
            }

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return uploadResult;
    }


    public static String getBlurrImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.URL + SEPARATOR + CloudinaryDetail.CLOUD_NAME + SEPARATOR + CloudinaryDetail.FOLDER + SEPARATOR + "/e_blur:800/" + "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + SEPARATOR + publicId + FORMAT;
//        Logger.e("imageUrl", url);
        return url;
    }

    public static HashMap<String, String> getCloudinaryParams(String imagePath) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_API_KEY, CloudinaryDetail.API_KEY);
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_API_SECRET, CloudinaryDetail.API_SECRET);
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_CLOUD_NAME, CloudinaryDetail.CLOUD_NAME);
        paramMap.put(Constants.REQUEST_KEY_CLOUDINARY_IMAGE_PATH, imagePath);
        return paramMap;
    }

}

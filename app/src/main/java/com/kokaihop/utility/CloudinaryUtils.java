package com.kokaihop.utility;


import com.cloudinary.Cloudinary;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class CloudinaryUtils {

    public static final String FORMAT = ".jpg";
    public static final String SEPARATOR = "/";
    public static final String COMMA = ",";


    public static String getImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.URL +SEPARATOR+ CloudinaryDetail.CLOUD_NAME +SEPARATOR+ CloudinaryDetail.FOLDER +SEPARATOR+ "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + SEPARATOR + publicId + FORMAT;
//        Logger.e("imageUrl", URL);
        return url;
    }

    public static String getRoundedImageUrl(String publicId, String width, String height) {
        String url = CloudinaryDetail.URL +SEPARATOR+ CloudinaryDetail.CLOUD_NAME + SEPARATOR+CloudinaryDetail.FOLDER +SEPARATOR+ "w_" + width + COMMA + "h_" + height + COMMA + "c_fill" + COMMA + "r_max" + COMMA + "g_face" + SEPARATOR + publicId + FORMAT;
//        Logger.e("imageUrl", URL);
        return url;
    }


    public Map<String, String> uploadImageOnCloudinary(HashMap<String, String> config, String imagePath) {
        Cloudinary cloudinary = new Cloudinary(config);
        Map<String, String> uploadResult = null;
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


}

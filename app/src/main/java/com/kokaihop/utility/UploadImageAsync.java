package com.kokaihop.utility;

import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rajendra Singh on 9/6/17.
 */

public class UploadImageAsync extends AsyncTask<Void, Void, Map<String, String>> {
    private final HashMap<String, String> configMap;
    private Context context;
    private onCompleteListener mOnCompleteListener;
    private String imagePath;

    public UploadImageAsync(Context context, HashMap<String, String> configMap, String imagePath, onCompleteListener onCompleteListener) {
        this.context = context;
        this.imagePath = imagePath;
        this.configMap = configMap;
        this.mOnCompleteListener = onCompleteListener;

    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {
        CloudinaryUtils cloudinaryUtils = new CloudinaryUtils();
        return cloudinaryUtils.uploadImageOnCloudinary(configMap, imagePath);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Map<String, String> uploadResult) {
/* Dismiss the progress dialog after sharing */
        mOnCompleteListener.onComplete(uploadResult);
    }


    /**
     * Inteface for the caller
     */
    public interface onCompleteListener {
        void onComplete(Map<String, String> uploadResult);
    }
}

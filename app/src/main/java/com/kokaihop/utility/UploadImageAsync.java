package com.kokaihop.utility;

import android.content.Context;
import android.os.AsyncTask;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rajendra Singh on 9/6/17.
 */

public class UploadImageAsync extends AsyncTask<Void, Void, Map<String, String>> {
    private final HashMap<String, String> configMap;
    private Context context;
    private OnCompleteListener mOnCompleteListener;

    public UploadImageAsync(Context context, HashMap<String, String> configMap, OnCompleteListener OnCompleteListener) {
        this.context = context;
        this.configMap = configMap;
        this.mOnCompleteListener = OnCompleteListener;

    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {
        CloudinaryUtils cloudinaryUtils = new CloudinaryUtils();
        return cloudinaryUtils.uploadImageOnCloudinary(configMap);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Map<String, String> uploadResult) {
/* Dismiss the progress dialog after sharing */
        try {
            mOnCompleteListener.onComplete(uploadResult);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Inteface for the caller
     */
    public interface OnCompleteListener {
        void onComplete(Map<String, String> uploadResult) throws ParseException;
    }
}

package com.kokaihop.authentication.forgotpassword;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */
public class ForgotApiResponse {

    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }
}

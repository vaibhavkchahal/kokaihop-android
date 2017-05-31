package com.kokaihop.authentication.forgotpassword;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */
public class ForgotApiResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("msg")
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

}

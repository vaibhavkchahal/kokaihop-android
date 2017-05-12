package com.kokaihop.authentication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */
public class AuthenticationApiResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    @SerializedName("errors")
    private ErrorEmail errorEmail;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public ErrorEmail getErrorEmail() {
        return errorEmail;
    }

    public class ErrorEmail {

        @SerializedName("email")
        private EmailErroDetail detail;

        public EmailErroDetail getDetail() {
            return detail;
        }

        public class EmailErroDetail {

            @SerializedName("message")
            private String message;

            public String getMessage() {
                return message;
            }
        }
    }
}

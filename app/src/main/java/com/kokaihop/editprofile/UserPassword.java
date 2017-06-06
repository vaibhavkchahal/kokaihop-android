package com.kokaihop.editprofile;

/**
 * Created by Rajendra Singh on 6/6/17.
 */

class UserPassword {
    private String changePassword;

    public UserPassword(String changePassword) {
        this.changePassword = changePassword;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }
}

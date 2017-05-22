package com.kokaihop.home.userprofile;

import com.kokaihop.home.userprofile.model.UserApiResponse;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public interface UserApiCallback {
    void showUserProfile(UserApiResponse userApiResponse);
}

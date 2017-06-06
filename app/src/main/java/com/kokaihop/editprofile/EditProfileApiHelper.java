package com.kokaihop.editprofile;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import retrofit2.Call;

/**
 * Created by Rajendra Singh on 30/5/17.
 */

public class EditProfileApiHelper {
    private EditUserProfileApi editUserProfileApi;

    public EditProfileApiHelper() {
        this.editUserProfileApi = RetrofitClient.getInstance().create(EditUserProfileApi.class);
    }


    public void changePassword(String accessToken, String userId, UserPassword password, final IApiRequestComplete successInterface){
        Call<ChangePasswordResponse> userResponseCall = editUserProfileApi.changePassword(accessToken,userId,password);
        userResponseCall.enqueue(new ResponseHandler<ChangePasswordResponse >(successInterface));
    }

}

package com.kokaihop.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public interface LoginApi {

    @FormUrlEncoded
    @POST("/auth/local")
    Call<LoginApiResponse> doLogin(@Field("email") String email,@Field("password") String password);


    @FormUrlEncoded
    @POST("v1/api/users/generateResetPasswordLink")
    Call<ForgotApiResponse> forgot(@Field("email") String email);

}

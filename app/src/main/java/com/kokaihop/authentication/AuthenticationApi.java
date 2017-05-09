package com.kokaihop.authentication;

import com.kokaihop.authentication.forgotpassword.ForgotApiResponse;
import com.kokaihop.authentication.login.LoginApiResponse;
import com.kokaihop.authentication.signup.SignUpSettings;
import com.kokaihop.city.CityLocation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Vaibhav Chahal on 3/5/17.
 */

public interface AuthenticationApi {

    @FormUrlEncoded
    @POST("/auth/local")
    Call<LoginApiResponse> doLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("v1/api/users/generateResetPasswordLink")
    Call<ForgotApiResponse> forgot(@Field("email") String email);


    @FormUrlEncoded
    @POST("/users")
    Call<ForgotApiResponse> signUp(@Field("email") String email, @Body CityLocation location, @Field("name")
            String name, @Body SignUpSettings settings, @Field("password") String password);

}

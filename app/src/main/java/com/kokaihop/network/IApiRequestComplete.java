package com.kokaihop.network;

/**
 * Created by Vaibhav Chahal on 4/5/17.
 */

public interface IApiRequestComplete<T> {

    public void onSuccess(T response);

    public void onFailure(String message);
}

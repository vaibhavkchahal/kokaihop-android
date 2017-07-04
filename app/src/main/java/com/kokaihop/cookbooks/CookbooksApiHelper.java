package com.kokaihop.cookbooks;

import com.kokaihop.cookbooks.model.CookbookName;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.network.ResponseHandler;
import com.kokaihop.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Rajendra Singh on 27/6/17.
 */

public class CookbooksApiHelper {

    private CookbooksApi cookbooksApi;

    public CookbooksApiHelper() {
        cookbooksApi = RetrofitClient.getInstance().create(CookbooksApi.class);
    }

    public void getRecipesOfCookbook(String collectionFriendlyUrl, String userFriendlyUrl, int offset, int max, final IApiRequestComplete successInterface) {
        Call<ResponseBody> recipeOfCookbook = cookbooksApi.getRecipeOfCookbook(collectionFriendlyUrl, userFriendlyUrl, offset, max);
        recipeOfCookbook.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }

    public void createCookbook(String accessToken, CookbookName cookbook, IApiRequestComplete successInterface) {
        Call<ResponseBody> recipeOfCookbook = cookbooksApi.createCookbook(accessToken, cookbook);
        recipeOfCookbook.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }
    public void deleteCookbook(String accessToken, String cookbookId, IApiRequestComplete successInterface) {
        Call<ResponseBody> deleteCookbook = cookbooksApi.deleteCookbook(accessToken, cookbookId);
        deleteCookbook.enqueue(new ResponseHandler<ResponseBody>(successInterface));
    }
}

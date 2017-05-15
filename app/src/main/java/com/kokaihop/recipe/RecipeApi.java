package com.kokaihop.recipe;

import kokaihop.databundle.RecipeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public interface RecipeApi {

    @GET("/v1/api/recipes?")
    Call<RecipeResponse> getRecipe(@Query("fetchFacets") int fetchFacets, @Query("max") int max,
                                   @Query("offset") int offset, @Query("sortParams") String sortParams,
                                   @Query("type") String type);
}
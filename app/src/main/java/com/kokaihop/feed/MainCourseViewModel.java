package com.kokaihop.feed;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.network.IApiRequestComplete;

/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class MainCourseViewModel extends BaseViewModel {

    private int offset = 0;
    private int max = 10;
    private boolean isLike = true;
    private String badgeType = "MAIN_COURSE_OF_THE_DAY";


    public MainCourseViewModel() {
        getRecipes();
    }

    public void getRecipes() {
        setProgressVisible(true);
        new FeedApiHelper().getRecepies(badgeType,isLike,offset,max,new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                setProgressVisible(false);
            }

            @Override
            public void onFailure(String message) {
                setProgressVisible(false);
            }

            @Override
            public void onError(RecipeResponse response) {
                setProgressVisible(false);
            }
        });

    }
}

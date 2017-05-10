package kokaihop.databundle;

import android.util.Log;

import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.database.Recipe;
import com.kokaihop.recipe.RecipeApiHelper;
import com.kokaihop.recipe.RecipeRequestParams;
import com.kokaihop.recipe.RecipeResponse;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.BaseViewModel;
import com.kokaihop.utility.RealmHelper;

import io.realm.Realm;

import static com.kokaihop.utility.RealmHelper.realm;

/**
 * Created by Rajendra Singh on 8/5/17.
 */

public class BundleViewModel extends BaseViewModel {
    RecipeApiHelper mRecipeApiHelper;
    RecipeRequestParams mRecipeRequestParams;
    Realm mRealm;

    public BundleViewModel() {
        mRecipeApiHelper = new RecipeApiHelper();
        mRecipeRequestParams = getmRecipeRequestParams();
        mRealm = RealmHelper.getRealmInstance();


    }

    public void getRecipe(int offset) {
        setProgressVisible(true);
        mRecipeRequestParams.setOffset(offset);
        Log.e("offset before request", String.valueOf(offset));

        mRecipeApiHelper.getRecipe(mRecipeRequestParams, new IApiRequestComplete<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse recipeResponse) {
                if (mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset() < recipeResponse.getCount()) {
                    int offset = mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset();
                    Log.e("offset after response", String.valueOf(offset));
                    Log.e("count", String.valueOf(recipeResponse.getCount()));
                    getRecipe(mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset());

                } else {
                    Log.e("end of recipe data", "end of recipe data");
                    Log.e("count", String.valueOf(recipeResponse.getCount()));
                    Log.e("offset", String.valueOf(mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset()));


                    setProgressVisible(false);
                }
                insertRecord(recipeResponse);
                long recordCount = realm.where(Recipe.class).count();
                Log.e("Saved record in DB", String.valueOf(recordCount));


            }

            @Override
            public void onFailure(String message) {
                Log.d("on Failure", message);

            }
        });


    }

    public RecipeRequestParams getmRecipeRequestParams() {
        RecipeRequestParams recipeRequestParams = new RecipeRequestParams();
        recipeRequestParams.setSortParams(ApiConstants.MOST_RECENT);
        recipeRequestParams.setMax(1000);
        recipeRequestParams.setFetchFacets(0);
        recipeRequestParams.setOffset(0);
        recipeRequestParams.setWithImages(0);
        recipeRequestParams.setType(ApiConstants.RecipeType.Recipe.name());
        return recipeRequestParams;
    }

    public void insertRecord(RecipeResponse recipeResponse) {

        mRealm.beginTransaction();
//        Collection<RecipeDetails> realmCities = realm.copyToRealm(recipeResponse.getRecipeDetailsList());
        mRealm.copyToRealmOrUpdate(recipeResponse.getRecipeDetailsList());
//        realm.insert(recipeResponse.getRecipeDetailsList());
        mRealm.commitTransaction();
    }
}

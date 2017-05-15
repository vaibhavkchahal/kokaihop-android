package kokaihop.databundle;

import android.util.Log;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.DBConstants;
import com.kokaihop.database.Recipe;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.recipe.RecipeApiHelper;
import com.kokaihop.recipe.RecipeRequestParams;
import com.kokaihop.utility.ApiConstants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.kokaihop.database.DBConstants.DATABASE_NAME;
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
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(DATABASE_NAME).
                schemaVersion(DBConstants.SCHEMA_VERSION).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        // Clear the realm from last time
//        Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        mRealm = Realm.getDefaultInstance();


    }

    public void getRecipe(int offset) {
        setProgressVisible(true);
        mRecipeRequestParams.setOffset(offset);
        Log.e("offset before request", String.valueOf(offset));

        mRecipeApiHelper.getRecipe(mRecipeRequestParams, new IApiRequestComplete<SearchResponse>() {
            @Override
            public void onSuccess(SearchResponse searchResponse) {
                if (mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset() < searchResponse.getCount()) {
                    int offset = mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset();
                    Log.e("offset after response", String.valueOf(offset));
                    Log.e("count", String.valueOf(searchResponse.getCount()));
                    getRecipe(mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset());

                } else {
                    Log.e("end of recipe data", "end of recipe data");
                    Log.e("count", String.valueOf(searchResponse.getCount()));
                    Log.e("offset", String.valueOf(mRecipeRequestParams.getMax() + mRecipeRequestParams.getOffset()));


                    setProgressVisible(false);
                }
                insertRecord(searchResponse);
                long recordCount = realm.where(Recipe.class).count();
                Log.e("Saved record in DB", String.valueOf(recordCount));


            }

            @Override
            public void onFailure(String message) {
                Log.d("on Failure", message);

            }

            @Override
            public void onError(SearchResponse response) {

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

    public void insertRecord(SearchResponse searchResponse) {

        mRealm.beginTransaction();
//        Collection<RecipeDetails> realmCities = realm.copyToRealm(recipeResponse.getRecipeDetailsList());
        mRealm.copyToRealmOrUpdate(searchResponse.getRecipeDetailsList());

//        realm.insert(recipeResponse.getRecipeDetailsList());
        mRealm.commitTransaction();
    }
}

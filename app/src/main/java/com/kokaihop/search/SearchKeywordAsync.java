package com.kokaihop.search;

import android.os.AsyncTask;

import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.utility.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.kokaihop.utility.RealmHelper.realm;

/**
 * Created by Rajendra Singh on 9/6/17.
 */

public class SearchKeywordAsync extends AsyncTask<Void, Void, List<RecipeRealmObject>> {

    private OnCompleteListener onCompleteListener;
    private RealmResults<RecipeRealmObject> recipeRealmResult;

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public SearchKeywordAsync(RealmResults<RecipeRealmObject> recipeRealmResult) {
        this.recipeRealmResult = recipeRealmResult;

    }

    @Override
    protected List<RecipeRealmObject> doInBackground(Void... params) {
        realm = Realm.getDefaultInstance();
        List<RecipeRealmObject> copyRealm = realm.copyFromRealm(recipeRealmResult, 1);
        sortByRating(copyRealm);
        return copyRealm;
    }

    private void sortByRating(List<RecipeRealmObject> copyRealm) {
        Collections.sort(copyRealm, Collections.reverseOrder(new Comparator<RecipeRealmObject>() {
            @Override
            public int compare(RecipeRealmObject recipeRealmObject1, RecipeRealmObject recipeRealmObject2) {
                if (recipeRealmObject1.getRating() != null && recipeRealmObject2.getRating() != null) {

                    if ((recipeRealmObject1.getRating().getRaters() >= 5 && recipeRealmObject2.getRating().getRaters() >= 5)
                            || (recipeRealmObject1.getRating().getRaters() < 5 && recipeRealmObject2.getRating().getRaters() < 5)) {
                        return Float.compare(recipeRealmObject1.getRating().getAverage(), recipeRealmObject2.getRating().getAverage());
                    } else if (recipeRealmObject1.getRating().getRaters() < 5 && recipeRealmObject2.getRating().getRaters() >= 5) {
                        return Float.compare(0, recipeRealmObject2.getRating().getAverage());
                    } else {
                        return Float.compare(recipeRealmObject1.getRating().getAverage(), 0);
                    }

                } else {
                    if (recipeRealmObject1.getRating() == null && recipeRealmObject2.getRating() == null) {
                        return 0;
                    } else if (recipeRealmObject1.getRating() == null) {
                        return Float.compare(0, recipeRealmObject2.getRating().getAverage());
                    } else {
                        return Float.compare(recipeRealmObject1.getRating().getAverage(), 0);
                    }
                }
            }

        }));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(List<RecipeRealmObject> uploadResult) {
        Logger.e("time after search async", new Date().toString());

        onCompleteListener.onSearchComplete(uploadResult);

    }


    /**
     * Inteface for the caller
     */
    public interface OnCompleteListener {
        void onSearchComplete(List<RecipeRealmObject> recipeList);
    }
}

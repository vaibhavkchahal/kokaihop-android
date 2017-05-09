package com.kokaihop.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.altaworks.kokaihop.ui.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kokaihop.recipe.RecipeDetails;
import com.kokaihop.recipe.RecipeResponse;
import com.kokaihop.utility.RealmBackupRestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView mGridView;
//    private CityAdapter mAdapter;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(RealmBackupRestore.EXPORT_REALM_FILE_NAME).schemaVersion(1).build();

        // Clear the realm from last time
//        Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        realm = Realm.getInstance(realmConfiguration);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load from file "cities.json" first time
       /* if (mAdapter == null) {
            List<RecipeDetails> cities = loadCities();

            //This is the GridView adapter
            mAdapter = new CityAdapter(this);
            mAdapter.setData(cities);

            //This is the GridView which will display the list of cities
            mGridView = (GridView) findViewById(R.id.cities_list);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(HomeActivity.this);
            mAdapter.notifyDataSetChanged();
            mGridView.invalidate();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private List<RecipeDetails> loadCities() {
        // In this case we're loading from local assets.
        // NOTE: could alternatively easily load from network
        InputStream stream;
        try {
            stream = getAssets().open("cities.json");
        } catch (IOException e) {
            return null;
        }

        Gson gson = new GsonBuilder().create();

        JsonElement json = new JsonParser().parse(new InputStreamReader(stream));
        RecipeResponse recipeResponse = gson.fromJson(json, new TypeToken<RecipeResponse>() {
        }.getType());

        // Open a transaction to store items into the realm
        // Use copyToRealm() to convert the objects into proper RealmObjects managed by Realm.
        realm.beginTransaction();
//        Collection<RecipeDetails> realmCities = realm.copyToRealm(recipeResponse.getRecipeDetailsList());
        realm.copyToRealmOrUpdate(recipeResponse.getRecipeDetailsList());
//        realm.insert(recipeResponse.getRecipeDetailsList());
        realm.commitTransaction();

        Collection<RecipeDetails> realmCities = realm.where(RecipeDetails.class).findAll();

        return new ArrayList<RecipeDetails>(realmCities);
    }

    public void updateCities() {
        // Pull all the cities from the realm
        RealmResults<RecipeDetails> cities = realm.where(RecipeDetails.class).findAll();
        // Put these items in the Adapter
//        mAdapter.setData(cities);
//        mAdapter.notifyDataSetChanged();
//        mGridView.invalidate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      /*  RecipeDetails modifiedCity = (RecipeDetails) mAdapter.getItem(position);

        // Acquire the RealmObject matching the name of the clicked City.
        final RecipeDetails city = realm.where(RecipeDetails.class).equalTo("title", modifiedCity.getTitle()).findFirst();

        // Create a transaction to increment the vote count for the selected City in the realm
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                city.setTitle(city.getTitle() + " Modified Name");
            }
        });

        updateCities();*/
    }
}

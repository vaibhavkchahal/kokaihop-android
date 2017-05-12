package com.kokaihop.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.database.Recipe;
import com.kokaihop.recipe.RecipeResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.kokaihop.utility.RealmHelper.realm;


public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private Button mButtonSignIn;
    private TextView mTextViewCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.recipe_list);
        mButtonSignIn = (Button) findViewById(R.id.button_signin);
        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Recipe> recipe = realm.where(Recipe.class).findAll();
        RecipeAdapter recipeAdapter = new RecipeAdapter(this);
        recipeAdapter.setData(recipe);
        mListView.setAdapter(recipeAdapter);
        mTextViewCount = (TextView) findViewById(R.id.textview_count);
        mTextViewCount.setText("Recipe Count: " + String.valueOf(recipe.size()));


        Log.e("Recipe loaded", recipe.isLoaded() + " ," + recipe.isValid() + " ," + recipe.isEmpty() + " ," + recipe.size());
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
    }

    private List<Recipe> loadCities() {
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

        Collection<Recipe> realmCities = realm.where(Recipe.class).findAll();

        return new ArrayList<Recipe>(realmCities);
    }

    public void updateCities() {
        // Pull all the cities from the realm
        RealmResults<Recipe> cities = realm.where(Recipe.class).findAll();
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

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

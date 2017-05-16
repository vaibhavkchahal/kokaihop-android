package kokaihop.databundle;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityDatabaseBundlingBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.database.Recipe;
import com.kokaihop.recipe.SearchResponse;
import com.kokaihop.utility.RealmHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.RealmResults;

import static com.kokaihop.utility.RealmHelper.realm;


public class DatabaseBundlingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDatabaseBundlingBinding databaseBundlingBinding = DataBindingUtil.setContentView(this, R.layout.activity_database_bundling);
        BundleViewModel bundleViewModel = new BundleViewModel();
        databaseBundlingBinding.setViewModel(bundleViewModel);
        bundleViewModel.getRecipe(0);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Load from file "cities.json" first time
      /*  if (mAdapter == null) {
            List<RecipeDetails> cities = loadCities();

            //This is the GridView adapter
            mAdapter = new RecipeAdapter(this);
            mAdapter.setData(cities);

            //This is the GridView which will display the list of cities
            listView = (ListView) findViewById(R.id.cities_list);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(DatabaseBundlingActivity.this);
            mAdapter.notifyDataSetChanged();
            listView.invalidate();
        }*/
       /* RealmBackupRestore realmBackupRestore = new RealmBackupRestore(MainActivity.this, realm);
        realmBackupRestore.backup();*/
    }

    @Override
    protected void onDestroy() {

        if (RealmHelper.getRealmInstance() != null) {
            RealmHelper.getRealmInstance().close();// Remember to close Realm when done.
        }
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
        SearchResponse searchResponse = gson.fromJson(json, new TypeToken<SearchResponse>() {
        }.getType());

        // Open a transaction to store items into the realm
        // Use copyToRealm() to convert the objects into proper RealmObjects managed by Realm.
        realm.beginTransaction();
//        Collection<RecipeDetails> realmCities = realm.copyToRealm(recipeResponse.getRecipeList());
        realm.copyToRealmOrUpdate(searchResponse.getRecipeDetailsList());
//        realm.insert(recipeResponse.getRecipeList());
        realm.commitTransaction();

        Collection<Recipe> realmCities = realm.where(Recipe.class).findAll();

        return new ArrayList<Recipe>(realmCities);
    }

    public void updateCities() {
        // Pull all the cities from the realm
        RealmResults<Recipe> cities = realm.where(Recipe.class).findAll();
        // Put these items in the Adapter
        mAdapter.setData(cities);
        mAdapter.notifyDataSetChanged();
        listView.invalidate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Recipe modifiedCity = (Recipe) mAdapter.getItem(position);

        // Acquire the RealmObject matching the name of the clicked City.
//        final RecipeDetails city = realm.where(RecipeDetails.class).equalTo("title", modifiedCity.getTitle()).findFirst();

        // Create a transaction to increment the vote count for the selected City in the realm
       /* realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                city.setTitle(city.getTitle() + " Modified Name");
            }
        });*/

//        updateCities();
    }


    public void insertRecord(SearchResponse searchResponse) {
        realm.beginTransaction();
//        Collection<RecipeDetails> realmCities = realm.copyToRealm(recipeResponse.getRecipeList());
        realm.copyToRealmOrUpdate(searchResponse.getRecipeDetailsList());
//        realm.insert(recipeResponse.getRecipeList());
        realm.commitTransaction();
    }
}

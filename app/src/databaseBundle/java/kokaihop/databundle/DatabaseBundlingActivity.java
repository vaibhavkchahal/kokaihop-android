package kokaihop.databundle;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityDatabaseBundlingBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.recipe.SearchResponse;

import io.realm.Realm;

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

        if (Realm.getDefaultInstance() != null) {
            Realm.getDefaultInstance().close();// Remember to close Realm when done.
        }
        super.onDestroy();
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecipeRealmObject modifiedCity = (RecipeRealmObject) mAdapter.getItem(position);

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
        realm.copyToRealmOrUpdate(searchResponse.getRecipeRealmObjects());
//        realm.insert(recipeResponse.getRecipeList());
        realm.commitTransaction();
    }
}

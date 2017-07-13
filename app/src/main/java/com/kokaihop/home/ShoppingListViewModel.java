package com.kokaihop.home;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.ShoppingListRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.utility.Constants.AUTHORIZATION_BEARER;

/**
 * Created by Vaibhav Chahal on 10/7/17.
 */
public class ShoppingListViewModel extends BaseViewModel {

    private ShoppingDataManager shoppingDataManager;
    private List<IngredientsRealmObject> ingredientsList = new ArrayList<>();
    private Context context;
    private int totalItemCount;
    private ShoppingListViewModel.IngredientsDatasetListener datasetListener;
    private String authorizationToken;

    public List<IngredientsRealmObject> getIngredientsList() {
        return ingredientsList;
    }

    public ShoppingListViewModel(Context context, IngredientsDatasetListener dataSetListener) {
        this.context = context;
        shoppingDataManager = new ShoppingDataManager();
        this.datasetListener = dataSetListener;
        authorizationToken = AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        fetchIngredientFromDB();
        fetchIngredientUnits();
        fetchShoppingListFromServer();
    }

    private void fetchShoppingListFromServer() {
        String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        if (!userId.isEmpty()) {
            new HomeApiHelper().getShoppingList(authorizationToken, userId, new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {
                    ShoppingListResponse shoppingApiResponse = (ShoppingListResponse) response;
                    shoppingDataManager.insertOrUpdateData(shoppingApiResponse.getShoppingListRealmObject());
                    fetchIngredientFromDB();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Object response) {
                }
            });
        }
    }

    private void fetchIngredientUnits() {
        new HomeApiHelper().getIngredientUnits(authorizationToken, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                ShoppingUnitResponse shoppingUnitResponse = (ShoppingUnitResponse) response;
                shoppingDataManager.updateShoppingIngredientList(shoppingUnitResponse.getUnits());
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object response) {
            }
        });
    }

    public void fetchIngredientFromDB() {
        ingredientsList.clear();
        ShoppingListRealmObject listRealmObject = shoppingDataManager.fetchShoppingRealmObject();
        if (listRealmObject != null) {
            ingredientsList.addAll(listRealmObject.getIngredients());
            totalItemCount = listRealmObject.getIngredients().size();
            datasetListener.onUpdateIngredientsList();
        }
    }

    public void updateIngredientOnServer() {
        ShoppingListRealmObject listRealmObject = shoppingDataManager.fetchShoppingRealmObject();
        List<IngredientsRealmObject> realmObjects = shoppingDataManager.fetchCopyIngredientRealmObjects(listRealmObject.getIngredients());
        List<IngredientsRealmObject> sycNeededIngreidentList = new ArrayList<>();
        for (IngredientsRealmObject object : realmObjects) {
            if (object.isServerSyncNeeded()) {
                sycNeededIngreidentList.add(object);
            }
        }
        SyncIngredientModel model = new SyncIngredientModel();
//        Gson gson = new Gson();
////        Type type = new TypeToken<List<IngredientsRealmObject>>() {
////        }.getType();
//        String json = gson.toJson(sycNeededIngreidentList.get(0));
//        System.out.println("json format->" + json);
        model.setRealmObjects(sycNeededIngreidentList);
        new HomeApiHelper().sycIngredientOnServer(authorizationToken, model, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                SyncIngredientModel ingredientModel = (SyncIngredientModel) response;
                Log.d("updated items size", "" + ingredientModel.getRealmObjects().size());
            }

            @Override
            public void onFailure(String message) {
            }

            @Override
            public void onError(Object response) {
            }
        });
    }

    public interface IngredientsDatasetListener {
        void onUpdateIngredientsList();
    }

    @Override
    protected void destroy() {
    }

}

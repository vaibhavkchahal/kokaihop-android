package com.kokaihop.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kokaihop.base.BaseViewModel;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.database.ShoppingListRealmObject;
import com.kokaihop.network.IApiRequestComplete;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.SharedPrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.kokaihop.utility.Constants.AUTHORIZATION_BEARER;

/**
 * Created by Vaibhav Chahal on 10/7/17.
 */
public class ShoppingListViewModel extends BaseViewModel {

    private String accessToken;
    private ShoppingDataManager shoppingDataManager;
    private List<IngredientsRealmObject> ingredientsList = new ArrayList<>();
    private Context context;
    private ShoppingListViewModel.IngredientsDatasetListener datasetListener;
    private String authorizationToken;
    private List<String> markedIds = new ArrayList<>();

    public List<IngredientsRealmObject> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<IngredientsRealmObject> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<String> getMarkedIds() {
        return markedIds;
    }

    public void setMarkedIds(List<String> markedIds) {
        this.markedIds = markedIds;
    }

    public ShoppingListViewModel(Context context, IngredientsDatasetListener dataSetListener) {
        this.context = context;
        shoppingDataManager = new ShoppingDataManager();
        this.datasetListener = dataSetListener;
        accessToken = SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
        authorizationToken = AUTHORIZATION_BEARER + accessToken;
        fetchIngredientFromDB();
        deleteIngredientOnServer();
        if (!TextUtils.isEmpty(accessToken)) {
            fetchIngredientUnits();

        }
        updateIngredientOnServer();
//        fetchShoppingListFromServer();
    }

    public ShoppingDataManager getShoppingDataManager() {
        return shoppingDataManager;
    }

    private void fetchShoppingListFromServer() {
        String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        if (!userId.isEmpty()) {
            new HomeApiHelper().getShoppingList(authorizationToken, userId, new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {
                    ShoppingListResponse shoppingApiResponse = (ShoppingListResponse) response;
                    shoppingDataManager.deletePreviousShoppingList();
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
                shoppingDataManager.updateShoppingIngredientUnitList(shoppingUnitResponse.getUnits());
                shoppingDataManager.updateShoppingIngredientUnitList(shoppingUnitResponse.getUnits());
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
            for (int i = 0; i < listRealmObject.getIngredients().size(); i++) {
                IngredientsRealmObject object = listRealmObject.getIngredients().get(i);
                if (!object.isDeletionNeeded()) {
                    for (int markPosition = 0; markPosition < markedIds.size(); markPosition++) {
                        if (object.get_id().equals(markedIds.get(markPosition))) {
                            shoppingDataManager.markIngredientObjectInDB(object.get_id());
                            break;
                        }
                    }
                    ingredientsList.add(object);
                }
            }
        }
        datasetListener.onUpdateIngredientsList();
        EventBus.getDefault().postSticky(new ShoppingListCounterEvent(ingredientsList.size()));
    }

    public void updateIngredientOnServer() {
        ShoppingListRealmObject listRealmObject = shoppingDataManager.fetchShoppingRealmObject();
        if (listRealmObject != null) {
            final List<IngredientsRealmObject> realmObjects = shoppingDataManager.fetchCopyIngredientRealmObjects(listRealmObject.getIngredients());
            List<IngredientsRealmObject> sycNeededIngreidentList = new ArrayList<>();
            ignoreIdFieldFromIngredientObject(realmObjects, sycNeededIngreidentList);
            SyncIngredientModel model = new SyncIngredientModel();
            model.setList(sycNeededIngreidentList);
            new HomeApiHelper().sycIngredientOnServer(authorizationToken, model, new IApiRequestComplete() {
                @Override
                public void onSuccess(Object response) {
                    SyncIngredientModel ingredientModel = (SyncIngredientModel) response;
                    Log.d("updated items size", "" + ingredientModel.getRealmObjects().size());
                    shoppingDataManager.removeIngredientFromRealmDatabase(realmObjects);
                    fetchShoppingListFromServer();
                }

                @Override
                public void onFailure(String message) {
                }

                @Override
                public void onError(Object response) {
                }
            });
        } else {
            fetchShoppingListFromServer();
        }
    }

    private void ignoreIdFieldFromIngredientObject(List<IngredientsRealmObject> realmObjects, List<IngredientsRealmObject> sycNeededIngreidentList) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(IngredientsRealmObject.class, new IngredientSerializer())
                .create();
        for (IngredientsRealmObject object : realmObjects) {
            if (object.isServerSyncNeeded()) {
                if (object.get_id().contains(Constants.TEMP_INGREDIENT_ID_SIGNATURE)) {
                    String result = gson.toJson(object);
                    IngredientsRealmObject realmObject = gson.fromJson(result, IngredientsRealmObject.class);
                    sycNeededIngreidentList.add(realmObject);
                } else {
                    sycNeededIngreidentList.add(object);
                }

            }
        }
    }

    public void deleteIngredientOnServer() {
        List<String> idsToDelete = new ArrayList<>();
        for (IngredientsRealmObject ingredientsRealmObject : shoppingDataManager.fetchShoppingRealmObject().getIngredients()) {
            if (ingredientsRealmObject.isDeletionNeeded()) {
                idsToDelete.add(ingredientsRealmObject.get_id());
            }
        }
        SyncIngredientDeletionModel requestParams = new SyncIngredientDeletionModel();
        requestParams.setIds(idsToDelete);
        new HomeApiHelper().sycIngredientDeletionOnServer(authorizationToken, requestParams, new IApiRequestComplete() {
            @Override
            public void onSuccess(Object response) {
                SyncIngredientModel model = (SyncIngredientModel) response;
                shoppingDataManager.updateShoppingIngredientList(model.getRealmObjects());
                fetchIngredientFromDB();
            }

            @Override
            public void onFailure(String message) {
            }

            @Override
            public void onError(Object response) {
            }
        });
    }

    public void onClickClearMarked(View view) {
        if (shoppingDataManager.isAnyMarkedObject()) {
            shoppingDataManager.deleteMarkedIngredientObjectFromDB();
            fetchIngredientFromDB();
            deleteIngredientOnServer();
            markedIds.clear();
        } else {
            AppUtility.showOkDialog(view.getContext(), context.getString(R.string.obs_text), context.getString(R.string.mark_message));
        }
    }

    public interface IngredientsDatasetListener {
        void onUpdateIngredientsList();
    }

    public void openEditScreen() {
    }

    @Override
    protected void destroy() {
    }

}

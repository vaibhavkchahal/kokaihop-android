package com.kokaihop.home;

import android.app.Activity;
import android.content.Context;
import android.view.View;
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

    public List<IngredientsRealmObject> getIngredientsList() {
        return ingredientsList;
    }

    public ShoppingListViewModel(Context context) {
        this.context = context;
        shoppingDataManager = new ShoppingDataManager();
//        fetchIngredientFromDB();
        fetchShoppingListFromServer();
    }

    private void fetchShoppingListFromServer() {
        String userId = SharedPrefUtils.getSharedPrefStringData(context, Constants.USER_ID);
        String authorizationToken = AUTHORIZATION_BEARER + SharedPrefUtils.getSharedPrefStringData(context, Constants.ACCESS_TOKEN);
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

    private void fetchIngredientFromDB() {
        ingredientsList.clear();
        ShoppingListRealmObject listRealmObject = shoppingDataManager.fetchShoppingRealmObject();
        ingredientsList.addAll(listRealmObject.getIngredients());
        totalItemCount = listRealmObject.getIngredients().size();
    }

    @Override
    protected void destroy() {
    }

    public void onBackPressed(View view) {
        ((Activity) (view.getContext())).finish();
    }
}

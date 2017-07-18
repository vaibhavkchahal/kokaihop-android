package com.kokaihop.home;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.database.IngredientsRealmObject;
import com.kokaihop.utility.Constants;

/**
 * Created by Vaibhav Chahal on 11/7/17.
 */

public class ShoppingListHandler {

    private ShoppingDataManager shoppingDataManager = new ShoppingDataManager();

    public void onListItemClick(View view, TextView name, View divider, ImageView edit, IngredientsRealmObject ingredientsRealmObject) {
        if (divider.getVisibility() == View.VISIBLE) {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white_FFF7F7F7));
            divider.setVisibility(View.GONE);
            edit.setImageResource(R.drawable.ic_tick_sm);
            edit.setTag(Constants.MARKED_INGREDIENT_TAG);
            shoppingDataManager.markIngredientObjectInDB(ingredientsRealmObject.get_id());
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
            divider.setVisibility(View.VISIBLE);
            name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            edit.setImageResource(R.drawable.ic_edit_md);
            edit.setTag(Constants.EDIT_INGGREDIENT_TAG);
            shoppingDataManager.UnMarkIngredientObjectInDB(ingredientsRealmObject.get_id());
        }
    }
}

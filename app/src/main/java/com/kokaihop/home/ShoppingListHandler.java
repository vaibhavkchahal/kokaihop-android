package com.kokaihop.home;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vaibhav Chahal on 11/7/17.
 */

public class ShoppingListHandler {

    public void onListItemClick(View view, TextView name, View divider, ImageView edit) {
        if (divider.getVisibility() == View.VISIBLE) {
            divider.setVisibility(View.GONE);
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            divider.setVisibility(View.VISIBLE);
            name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
//        if()
    }
}

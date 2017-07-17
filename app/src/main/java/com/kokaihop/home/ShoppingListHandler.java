package com.kokaihop.home;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;

/**
 * Created by Vaibhav Chahal on 11/7/17.
 */

public class ShoppingListHandler {

    public void onListItemClick(View view, TextView name, View divider, ImageView edit) {
        if (divider.getVisibility() == View.VISIBLE) {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white_FFF7F7F7));
            divider.setVisibility(View.GONE);
            edit.setImageResource(R.drawable.ic_tick_sm);
            edit.setTag(R.drawable.ic_tick_sm);
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
            divider.setVisibility(View.VISIBLE);
            name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            edit.setImageResource(R.drawable.ic_edit_md);
            edit.setTag(R.drawable.ic_edit_md);
        }
    }
}

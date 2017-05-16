package com.kokaihop.feed;

import android.databinding.ObservableArrayList;
import android.view.View;

/**
 * Created by Vaibhav Chahal on 15/5/17.
 */

public class RecipeItemList {

    public ObservableArrayList<RecipeItemInfo> list = new ObservableArrayList<>();
    private int mTotalCount;

    public RecipeItemList() {
        for (mTotalCount = 1; mTotalCount < 51; ++mTotalCount) {
            add(new RecipeItemInfo("item_" + (mTotalCount)));
        }
    }

    // Called on add button click
    public void add(View v) {
        list.add(new RecipeItemInfo("icon_" + mTotalCount++));
    }

    // Called on remove button click
    public void remove(View v) {
        if (!list.isEmpty()) {
            list.remove(0);
        }
    }

    private void add(RecipeItemInfo info) {
        list.add(info);
    }
}
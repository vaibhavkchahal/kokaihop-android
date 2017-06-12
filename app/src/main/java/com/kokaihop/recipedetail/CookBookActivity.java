package com.kokaihop.recipedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityCookbookBinding;
import com.kokaihop.base.BaseActivity;

/**
 * Created by Vaibhav Chahal on 12/6/17.
 */

public class CookBookActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCookbookBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cookbook);
    }
}

package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityAddIngredientsBinding;
import com.kokaihop.base.BaseActivity;

/**
 * Created by Vaibhav Chahal on 12/7/17.
 */

public class AddIngredientActivity extends BaseActivity {

    private ActivityAddIngredientsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ingredients);
        binding.setViewModel(new AddIngredientViewModel());
    }
}

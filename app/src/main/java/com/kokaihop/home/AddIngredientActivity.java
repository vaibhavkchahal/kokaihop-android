package com.kokaihop.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityAddIngredientsBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.Constants;

/**
 * Created by Vaibhav Chahal on 12/7/17.
 */

public class AddIngredientActivity extends BaseActivity implements AddIngredientViewModel.UnitDataListener {

    private ActivityAddIngredientsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ingredients);
        checkIfIntentHasData();
        binding.setViewModel(new AddIngredientViewModel(this, this));
    }

    private void checkIfIntentHasData() {
        Intent intent = getIntent();
        if (intent.getStringExtra(Constants.INGREDIENT_NAME) != null) {
            binding.edittextEnterIngredient.setText(intent.getStringExtra(Constants.INGREDIENT_NAME));
            GoogleAnalyticsHelper.trackScreenName(getString(R.string.ingredient_edit_screen));
        }
        else
        {
            GoogleAnalyticsHelper.trackScreenName(getString(R.string.ingredient_add_screen));
        }
        if (intent.getFloatExtra(Constants.INGREDIENT_AMOUNT, 0) != 0) {
            binding.edittextQuantity.setText(String.valueOf(intent.getFloatExtra(Constants.INGREDIENT_AMOUNT, 0)));
        }
        if (intent.getStringExtra(Constants.INGREDIENT_UNIT) != null) {
            binding.txtviewUnitValue.setText(intent.getStringExtra(Constants.INGREDIENT_UNIT));
        }
    }

    @Override
    public void onUnitPickerValueChange(String value) {
        binding.txtviewUnitValue.setText(value);
    }
}

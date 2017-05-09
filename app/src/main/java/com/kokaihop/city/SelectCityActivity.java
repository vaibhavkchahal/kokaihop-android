package com.kokaihop.city;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySelectCityBinding;
import com.kokaihop.utility.BaseActivity;

public class SelectCityActivity extends BaseActivity implements SelectCityInterface{

    SelectCityViewModel selectCityViewModel;
    ActivitySelectCityBinding selectCityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectCityViewModel = new SelectCityViewModel(this);
        selectCityBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_city);
        selectCityBinding.setSelectCityViewModel(selectCityViewModel);
    }

    @Override
    public void citySelected(CityDetails selectedCity) {
        Log.e("Selected City",selectedCity.getName());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("citySelected",selectedCity);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void setCitiesOnRecyclerView() {
        RecyclerView cityListRecyclerView = selectCityBinding.recyclerViewCityList;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        SelectCityAdapter cityAdapter = new SelectCityAdapter(selectCityViewModel.getCityList(), this);
        cityListRecyclerView.setLayoutManager(layoutManager);
        cityListRecyclerView.setAdapter(cityAdapter);
    }
}

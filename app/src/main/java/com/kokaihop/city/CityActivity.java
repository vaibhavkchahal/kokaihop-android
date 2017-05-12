package com.kokaihop.city;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySelectCityBinding;
import com.kokaihop.base.BaseActivity;

public class CityActivity extends BaseActivity implements CityViewModel.CityInterface, android.support.v7.widget.SearchView.OnQueryTextListener, View.OnClickListener{

    CityViewModel cityViewModel;
    ActivitySelectCityBinding selectCityBinding;
    CityAdapter cityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityViewModel = new CityViewModel(this);
        selectCityBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_city);
        selectCityBinding.setSelectCityViewModel(cityViewModel);
//        setSupportActionBar(selectCityBinding.toolbarTop);

        selectCityBinding.searchviewSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCityBinding.searchviewSearchCity.setIconified(false);
            }
        });

        selectCityBinding.searchviewSearchCity.setOnQueryTextListener(this);
        selectCityBinding.toolbarCancel.setOnClickListener(this);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cityAdapter = new CityAdapter(cityViewModel.getCityList(), this);
        cityListRecyclerView.setLayoutManager(layoutManager);
        cityListRecyclerView.setAdapter(cityAdapter);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        cityAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        cityAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}

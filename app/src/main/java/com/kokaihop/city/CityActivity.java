package com.kokaihop.city;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySelectCityBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;

import java.lang.reflect.Field;

public class CityActivity extends BaseActivity implements CityViewModel.CityInterface, android.support.v7.widget.SearchView.OnQueryTextListener, View.OnClickListener {

    CityViewModel cityViewModel;
    ActivitySelectCityBinding selectCityBinding;
    CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityViewModel = new CityViewModel(this);
        selectCityBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_city);
        selectCityBinding.setViewModel(cityViewModel);
        initializeSearchView();
        selectCityBinding.toolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GoogleAnalyticsHelper.trackScreenName(this, getString(R.string.city_list_screen));


    }

    private void initializeSearchView() {
        TextView searchText = (TextView)
                selectCityBinding.searchviewSearchCity.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        selectCityBinding.searchviewSearchCity.onActionViewExpanded();
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Regular.ttf");
        searchText.setTypeface(myCustomFont);
        searchText.setTextColor(ContextCompat.getColor(CityActivity.this, R.color.grey_FF999999));
        searchText.setHintTextColor(ContextCompat.getColor(CityActivity.this, R.color.grey_FF999999));
        selectCityBinding.searchviewSearchCity.setQueryHint(getString(R.string.search));

        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchText, R.drawable.cursor_orange); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }

        selectCityBinding.searchviewSearchCity.setOnQueryTextListener(this);

    }

    @Override
    public void citySelected(CityDetails selectedCity) {
        Log.e("Selected City", selectedCity.getName());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("citySelected", selectedCity);
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
        if (cityAdapter != null)
            cityAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (cityAdapter != null)
            cityAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}

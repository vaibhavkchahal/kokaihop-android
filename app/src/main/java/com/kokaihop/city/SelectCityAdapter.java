package com.kokaihop.city;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowCityBinding;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.CityViewHolder> {

    private ArrayList<CityDetails> cityList;
    RowCityBinding rowCityBinding;
    SelectCityInterface selectCityInterface;

    public SelectCityAdapter(@NonNull ArrayList<CityDetails> cityList, SelectCityInterface selectCityInterface) {
        this.cityList = cityList;
        this.selectCityInterface = selectCityInterface;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowCityBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_city, parent, false);
        return new CityViewHolder(rowCityBinding);
    }

    @Override
    public void onBindViewHolder(final CityViewHolder holder, int position) {

        final String city = cityList.get(position).getName();
        rowCityBinding.textViewCity.setText(city);

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CityViewHolder(RowCityBinding rowCityBinding) {
            super(rowCityBinding.getRoot());
            rowCityBinding.textViewCity.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String city = (String) ((TextView) v).getText();
            selectCityInterface.citySelected(cityList.get(getAdapterPosition()));
            Log.e("city",city);
        }
    }
}

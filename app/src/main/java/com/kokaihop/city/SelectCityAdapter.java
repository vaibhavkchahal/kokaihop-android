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

public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.ViewHolder> {

    private ArrayList<CityDetails> cityList;
    SelectCityInterface selectCityInterface;

    public SelectCityAdapter(@NonNull ArrayList<CityDetails> cityList, SelectCityInterface selectCityInterface) {
        this.cityList = cityList;
        this.selectCityInterface = selectCityInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCityBinding rowCityBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_city, parent, false);
        return new ViewHolder(rowCityBinding);
    }

    @Override
    public void onBindViewHolder(SelectCityAdapter.ViewHolder holder, int position) {
        final CityDetails city = cityList.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowCityBinding rowCityBinding;

        public ViewHolder(RowCityBinding rowCityBinding) {
            super(rowCityBinding.getRoot());
            this.rowCityBinding = rowCityBinding;
            rowCityBinding.textViewCity.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String city = (String) ((TextView) v).getText();
            selectCityInterface.citySelected(cityList.get(getAdapterPosition()));
            Log.e("city", city);
        }

        public void bind(CityDetails city) {
            rowCityBinding.setCity(city);
            rowCityBinding.executePendingBindings();
        }
    }
}

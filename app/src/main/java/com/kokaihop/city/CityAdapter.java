package com.kokaihop.city;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowCityBinding;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class    CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> implements Filterable{

    private ArrayList<CityDetails> cityList;
    private ArrayList<CityDetails> OriginalcityList;
    CityViewModel.CityInterface cityInterface;

    public CityAdapter(@NonNull ArrayList<CityDetails> cityList, CityViewModel.CityInterface cityInterface) {
        this.cityList = cityList;
        this.cityInterface = cityInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowCityBinding rowCityBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_city, parent, false);
        return new ViewHolder(rowCityBinding);
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder holder, int position) {
        final CityDetails city = cityList.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<CityDetails> filteredCityList = new ArrayList<>();

                if(OriginalcityList== null){
                    OriginalcityList = cityList;
                }
                if(constraint!=null && OriginalcityList != null && OriginalcityList.size()>0){
                    for(final CityDetails city : OriginalcityList){
                        if(city.getName().toLowerCase().contains(constraint)){
                            filteredCityList.add(city);
                        }
                    }
                }
                filterResults.values = filteredCityList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cityList = (ArrayList<CityDetails>) results.values;
                notifyDataSetChanged();
            }
        };
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
            cityInterface.citySelected(cityList.get(getAdapterPosition()));
            Log.e("city", city);
        }

        public void bind(CityDetails city) {
            rowCityBinding.setCity(city);
            rowCityBinding.executePendingBindings();
        }
    }
}

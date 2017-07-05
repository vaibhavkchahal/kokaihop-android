package com.kokaihop.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowFilterDataBinding;

import java.util.List;

/**
 * Created by Rajendra Singh on 21/6/17.
 */

public class SearchFilterAdapter extends RecyclerView.Adapter<SearchFilterAdapter.ViewHolder> {

    private List<FilterData> filterDataList;
    private RowFilterDataBinding binding;
    private Context context;
    private String currentSelected;
    private FilterDataItemClickListener filterDataItemClickListener;

    public SearchFilterAdapter(List<FilterData> filterDataList, String currentSelected, FilterDataItemClickListener filterDataItemClickListener) {
        this.filterDataList = filterDataList;
        this.currentSelected = currentSelected;
        this.filterDataItemClickListener = filterDataItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_filter_data, parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FilterData filterData = this.filterDataList.get(position);
        if (currentSelected.equals(context.getString(R.string.all))
                || currentSelected.equals(context.getString(R.string.label_course))
                || currentSelected.equals(context.getString(R.string.label_cuisine))
                || currentSelected.equals(context.getString(R.string.label_method))) {
            FilterData filterDataAll = filterDataList.get(0);
            filterDataAll.setCurrentSelected(true);
        } else if (filterData.getName().equals(currentSelected)) {
            filterData.setCurrentSelected(true);
        } else {
            filterData.setCurrentSelected(false);
        }

        binding.relativeLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilterList();

                filterData.setCurrentSelected(true);
                filterDataItemClickListener.onItemClick(filterData);

            }
        });
        holder.bind(filterData);
        binding.executePendingBindings();


    }

    private void updateFilterList() {
        for (FilterData filterData : filterDataList
                ) {
            filterData.setCurrentSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return filterDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowFilterDataBinding binding;

        public ViewHolder(RowFilterDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(FilterData filterData) {
            binding.textviewName.setText(filterData.getName());

            if (filterData.isCurrentSelected()) {
                binding.imageviewSelected.setVisibility(View.VISIBLE);
            } else {
                binding.imageviewSelected.setVisibility(View.GONE);
            }

        }
    }

    public interface FilterDataItemClickListener {
        void onItemClick(FilterData filterData);

    }
}

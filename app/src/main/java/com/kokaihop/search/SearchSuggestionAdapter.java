package com.kokaihop.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.RowSuggestionKeywordBinding;
import com.kokaihop.database.SearchSuggestionRealmObject;

import java.util.List;

/**
 * Created by Rajendra Singh on 21/6/17.
 */

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder> {

    private List<SearchSuggestionRealmObject> suggestionsDataList;
    private RowSuggestionKeywordBinding binding;
    private Context context;
    private SuggestionDataItemClickListener suggestionDataItemClickListener;

    public SearchSuggestionAdapter(List<SearchSuggestionRealmObject> suggestionsDataList, SuggestionDataItemClickListener suggestionDataItemClickListener) {
        this.suggestionsDataList = suggestionsDataList;
        this.suggestionDataItemClickListener = suggestionDataItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_suggestion_keyword, parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SearchSuggestionRealmObject filterData = this.suggestionsDataList.get(position);

        binding.relativeLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, filterData.getKeyword(), Toast.LENGTH_SHORT).show();
                suggestionDataItemClickListener.onItemClick(filterData);

            }
        });
        holder.bind(filterData);
        binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return suggestionsDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowSuggestionKeywordBinding binding;

        public ViewHolder(RowSuggestionKeywordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(SearchSuggestionRealmObject filterData) {
            binding.textviewName.setText(filterData.getKeyword());
        }
    }

    public interface SuggestionDataItemClickListener {
        void onItemClick(SearchSuggestionRealmObject filterData);

    }
}

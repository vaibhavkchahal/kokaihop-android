package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentListBinding;
import com.kokaihop.utility.HorizontalDividerItemDecoration;

public class ListFragment extends Fragment implements ShoppingListViewModel.IngredientsDatasetListener {

    private ShoppingListViewModel viewModel;
    private FragmentListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        viewModel = new ShoppingListViewModel(getContext(), this);
        binding.setViewModel(viewModel);
        initializerecyclerView();
        return binding.getRoot();
    }

    private void initializerecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = binding.rvRecipeIngredients;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(getContext()));
        ShoppingListRecyclerAdapter adapter = new ShoppingListRecyclerAdapter(viewModel.getIngredientsList());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onUpdateIngredientsList() {
        RecyclerView recyclerView = binding.rvRecipeIngredients;
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}

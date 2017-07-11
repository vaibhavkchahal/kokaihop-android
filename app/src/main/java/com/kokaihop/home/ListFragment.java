package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

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
        viewModel = new ShoppingListViewModel(getContext());
        binding.setViewModel(viewModel);
        initializerecyclerView();
        return binding.getRoot();
    }

    private void initializerecyclerView() {
        RecyclerView recyclerView = binding.rvRecipeIngredients;
        ShoppingListRecyclerAdapter adapter = new ShoppingListRecyclerAdapter(viewModel.getIngredientsList());
        recyclerView.setAdapter(adapter);
    }
}

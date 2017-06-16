package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentHistoryRecipeBinding;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.RecipeDataManager;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.RecyclerViewScrollListener;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    private static RecipeFragment fragment;
    private FragmentHistoryRecipeBinding binding;
    private RecipeViewModel viewModel;
    private RecipeHistoryAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    public RecipeFragment() {
        // Required empty public constructor
    }

    public static RecipeFragment getInstance() {
        if (fragment == null) {
            fragment = new RecipeFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_recipe, container, false);
        viewModel = new RecipeViewModel(this, getContext());
        adapter = new RecipeHistoryAdapter(this, User.getInstance().getRecipesList());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = binding.rvHistoryList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewModel.getRecipesOfUsers(0);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (viewModel.getOffset() + viewModel.getMax() <= viewModel.getTotalRecipes())
                    viewModel.getRecipesOfUsers(viewModel.getOffset() + viewModel.getMax());
            }
        });
        return binding.getRoot();
    }

//    notify the adapter about the chage in data.
    public void showUserProfile() {
        adapter.notifyDataSetChanged();
    }
}

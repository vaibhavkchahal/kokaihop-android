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

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private static HistoryFragment fragment;
    private FragmentHistoryRecipeBinding binding;
    private RecipeHistoryAdapter adapter;
    private ArrayList<Recipe> recipes;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecipeDataManager recipeDataManager;


    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment getInstance() {
        if (fragment == null) {
            fragment = new HistoryFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_recipe, container, false);
        recipes = new ArrayList<>();
        adapter = new RecipeHistoryAdapter(this, recipes);
        recipeDataManager = new RecipeDataManager();
        adapter.displayHistoryChanges();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = binding.rvHistoryList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }
}
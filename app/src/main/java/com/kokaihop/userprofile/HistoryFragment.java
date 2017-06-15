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
import com.altaworks.kokaihop.ui.databinding.FragmentHistoryBinding;
import com.kokaihop.database.RecipeHistoryRealmObject;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.feed.Recipe;
import com.kokaihop.feed.RecipeDataManager;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private static HistoryFragment fragment;
    private FragmentHistoryBinding binding;
    private HistoryViewModel viewModel;
    private HistoryAdapter adapter;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        recipes = new ArrayList<>();
        viewModel = new HistoryViewModel();
        adapter = new HistoryAdapter(recipes, viewModel);
        recipeDataManager = new RecipeDataManager();
        updateHistory();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = binding.rvHistoryList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    private void updateHistory() {
        ArrayList<RecipeHistoryRealmObject> historyList = new HistoryDataManager().getHistory();
        for (RecipeHistoryRealmObject historyRealmObject : historyList) {
            RecipeRealmObject recipeRealmObject = recipeDataManager.fetchRecipe(historyRealmObject.getId());
            recipes.add(recipeDataManager.getRecipe(recipeRealmObject));
        }
        adapter.notifyDataSetChanged();
    }
}
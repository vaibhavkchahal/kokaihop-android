package com.kokaihop.userprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentHistoryRecipeBinding;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;

public class RecipeFragment extends Fragment {

    private FragmentHistoryRecipeBinding binding;
    private RecipeViewModel viewModel;
    private RecipeHistoryAdapter adapter;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private View noData;
    LayoutInflater inflater;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        String userId = bundle.getString(Constants.USER_ID);
        this.inflater = inflater;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_recipe, container, false);
        viewModel = new RecipeViewModel(this, getContext(), userId);
        adapter = new RecipeHistoryAdapter(this, User.getInstance().getRecipesList());
        layoutManager = new CustomLinearLayoutManager(getContext());
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

            @Override
            public void getScrolledState(RecyclerView recyclerView) {
            }
        });
        return binding.getRoot();
    }

    //    notify the adapter about the change in data.
    public void showUserProfile() {
        adapter.notifyDataSetChanged();
        if (noData == null) {
            noData = inflater.inflate(R.layout.layout_no_data_available, binding.recipeContainer, false);
        }
        if (adapter.getItemCount() <= 0) {
            if (noData.getParent() == null) {
                binding.recipeContainer.removeView(noData);
                binding.recipeContainer.addView(noData, 0);
            }
        } else {
            binding.recipeContainer.removeView(noData);
        }
    }
}

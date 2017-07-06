package com.kokaihop.cookbooks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCookbookDetailBinding;
import com.kokaihop.userprofile.RecipeHistoryAdapter;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;
import com.kokaihop.utility.SharedPrefUtils;

public class CookbookDetailFragment extends Fragment implements CookbookDataChangedListener{

    private FragmentCookbookDetailBinding binding;
    private CookbookDetailViewModel viewModel;
    private RecipeHistoryAdapter adapter;
    String userFriendlyUrl, cookbookFriendlyUrl, cookbookTitle;

    public CookbookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        User user;
        Bundle bundle = getArguments();
        if (bundle != null) {
            userFriendlyUrl = bundle.getString(Constants.USER_FRIENDLY_URL);
            cookbookFriendlyUrl = bundle.getString(Constants.COOKBOOK_FRIENDLY_URL);
            cookbookTitle = bundle.getString(Constants.COOKBOOK_TITLE);
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbook_detail, container, false);

        if (Constants.FAVORITE_RECIPE_FRIENDLY_URL.equals(cookbookFriendlyUrl)) {
            binding.btnDeleteCookbook.setVisibility(View.GONE);
        }
        if (!SharedPrefUtils.getSharedPrefStringData(getActivity(), Constants.FRIENDLY_URL).equals(userFriendlyUrl)) {
            binding.btnDeleteCookbook.setVisibility(View.GONE);
            binding.tvCookbookRename.setVisibility(View.GONE);
            binding.tvCookbookEdit.setVisibility(View.GONE);
            user = new User();
        } else {
            user = User.getInstance();
        }

        viewModel = new CookbookDetailViewModel(this, getContext(), user);
        binding.setCookbookTitle(cookbookTitle);
        binding.setViewModel(viewModel);
        adapter = new RecipeHistoryAdapter(this, user.getRecipesList());
        adapter.setViewModel(viewModel);
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
        RecyclerView recyclerView = binding.rvRecipesOfCookbook;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.getRecipesOfCookbook(cookbookFriendlyUrl, userFriendlyUrl, 0);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (viewModel.getOffset() + viewModel.getMax() <= viewModel.getTotalRecipes())
                    viewModel.getRecipesOfCookbook(cookbookFriendlyUrl, userFriendlyUrl, viewModel.getOffset() + viewModel.getMax());
            }
        });
        binding.tvCookbookEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.tvCookbookEdit.getText().toString().equals(getString(R.string.edit))) {
                    if(!cookbookFriendlyUrl.equals(Constants.FAVORITE_RECIPE_FRIENDLY_URL)){
                        binding.ivCookbookBack.setVisibility(View.GONE);
                        binding.tvCookbookRename.setVisibility(View.VISIBLE);
                    }
                    binding.tvCookbookEdit.setText(R.string.done);
                    adapter.setEditCookbook(true);
                } else {
                    binding.ivCookbookBack.setVisibility(View.VISIBLE);
                    binding.tvCookbookRename.setVisibility(View.GONE);
                    binding.tvCookbookEdit.setText(R.string.edit);
                    adapter.removeDeleteButton();
                    adapter.setEditCookbook(false);
                }
            }
        });
        return binding.getRoot();
    }

    public void showCookbookDetails() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(int position) {
        adapter.removeRecipe(position);
    }
}

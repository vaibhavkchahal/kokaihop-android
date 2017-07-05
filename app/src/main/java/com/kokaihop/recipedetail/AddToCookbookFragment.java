package com.kokaihop.recipedetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCookbooksBinding;
import com.kokaihop.userprofile.model.Cookbook;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;
import com.kokaihop.utility.SharedPrefUtils;

import java.util.ArrayList;

public class AddToCookbookFragment extends Fragment {

    private AddToCookbookAdapter adapter;
    private AddToCookbookViewModel viewModel;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    User user;

    public AddToCookbookFragment() {
        // Required empty public constructor
        user = User.getInstance();
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
        String friendlyUrl = SharedPrefUtils.getSharedPrefStringData(getActivity(),Constants.FRIENDLY_URL);
        String collectionMapping = bundle.getString(Constants.COLLECTION_MAPPING);
        String recipeId = bundle.getString(Constants.RECIPE_ID);
        viewModel = new AddToCookbookViewModel(this, getContext());
        final FragmentCookbooksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbooks, container, false);
        adapter = new AddToCookbookAdapter(user.getCookbooks(),friendlyUrl, collectionMapping, recipeId);
        layoutManager = new CustomLinearLayoutManager(getContext());
        recyclerView = binding.rvHistoryList;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewModel.getCookbooksOfUser(0);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (viewModel.getOffset() + viewModel.getMax() <= viewModel.getTotalCount())
                    viewModel.getCookbooksOfUser(viewModel.getOffset() + viewModel.getMax());
            }
        });
        binding.srlCookbooks.setEnabled(false);

        return binding.getRoot();
    }

    //    notify the adapter about the chage in data.
    public void displayCookbooks(ArrayList<Cookbook> cookbooks) {
        user.getCookbooks().clear();
        user.getCookbooks().addAll(cookbooks);
        adapter.notifyDataSetChanged();
    }
}

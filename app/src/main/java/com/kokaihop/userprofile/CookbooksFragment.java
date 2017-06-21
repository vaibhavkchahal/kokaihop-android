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
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.RecyclerViewScrollListener;

public class CookbooksFragment extends Fragment {

    private FragmentHistoryRecipeBinding binding;
    private CookbooksAdapter adapter;
    CookbooksViewModel viewModel;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    public CookbooksFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_recipe, container, false);
        viewModel = new CookbooksViewModel(this, getContext(), userId);
        adapter = new CookbooksAdapter(this, User.getOtherUser().getCookbooks());

        layoutManager = new LinearLayoutManager(getContext());
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
        return binding.getRoot();
    }

    //    notify the adapter about the chage in data.
    public void showUserProfile() {
        adapter.notifyDataSetChanged();
    }
}

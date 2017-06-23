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
import com.altaworks.kokaihop.ui.databinding.FragmentCookbookLoginBinding;
import com.altaworks.kokaihop.ui.databinding.FragmentCookbooksBinding;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.RecyclerViewScrollListener;
import com.kokaihop.utility.SharedPrefUtils;

public class CookbooksFragment extends Fragment {

    private CookbooksAdapter adapter;
    private CookbooksViewModel viewModel;
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
        boolean myCookbook = false;
        String accessToken = SharedPrefUtils.getSharedPrefStringData(getContext(), Constants.ACCESS_TOKEN);
        String userId = null;

        if (bundle != null) {
            userId = bundle.getString(Constants.USER_ID);
            myCookbook = bundle.getBoolean(Constants.MY_COOKBOOK);
        }

        viewModel = new CookbooksViewModel(this, getContext(), userId);

        if (myCookbook && (accessToken == null) || accessToken.isEmpty()) {
            FragmentCookbookLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbook_login, container, false);
            binding.setViewModel(viewModel);
            return binding.getRoot();
        } else {
            FragmentCookbooksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbooks, container, false);
            adapter = new CookbooksAdapter(this, User.getOtherUser().getCookbooks(), myCookbook);
            if (!myCookbook) {
                binding.toolbar.setVisibility(View.GONE);
            }
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
    }

    //    notify the adapter about the chage in data.
    public void showUserProfile() {
        adapter.notifyDataSetChanged();
    }
}

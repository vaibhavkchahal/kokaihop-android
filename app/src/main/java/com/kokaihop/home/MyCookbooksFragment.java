package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCookbookLoginBinding;
import com.altaworks.kokaihop.ui.databinding.FragmentCookbooksBinding;
import com.kokaihop.userprofile.CookbooksAdapter;
import com.kokaihop.userprofile.CookbooksViewModel;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;
import com.kokaihop.utility.SharedPrefUtils;

public class MyCookbooksFragment extends Fragment {

    private CookbooksAdapter adapter;
    private CookbooksViewModel viewModel;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    public MyCookbooksFragment() {
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
        boolean myCookbook = true;
        String accessToken = SharedPrefUtils.getSharedPrefStringData(getContext(), Constants.ACCESS_TOKEN);
        String userId = null;

        if (bundle != null) {
            userId = bundle.getString(Constants.USER_ID);
        }

        viewModel = new CookbooksViewModel(this, getContext(), userId);

        if (myCookbook && (accessToken == null) || accessToken.isEmpty()) {
            FragmentCookbookLoginBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbook_login, container, false);
            binding.setViewModel(viewModel);
            return binding.getRoot();
        } else {
            final FragmentCookbooksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbooks, container, false);
            binding.setViewModel(viewModel);
            adapter = new CookbooksAdapter(this, User.getInstance().getCookbooks(), myCookbook);
            if (!myCookbook) {
                binding.toolbar.setVisibility(View.GONE);
            }
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

            binding.srlCookbooks.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    viewModel.setDownloading(true);
                    viewModel.getCookbooksOfUser(0);
                    binding.srlCookbooks.setRefreshing(false);
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

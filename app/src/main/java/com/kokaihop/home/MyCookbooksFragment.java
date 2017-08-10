package com.kokaihop.home;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCookbookLoginBinding;
import com.altaworks.kokaihop.ui.databinding.FragmentMyCookbooksBinding;
import com.kokaihop.cookbooks.MyCookbooksViewModel;
import com.kokaihop.cookbooks.model.CookbooksList;
import com.kokaihop.userprofile.CookbooksAdapter;
import com.kokaihop.userprofile.model.User;
import com.kokaihop.utility.Constants;
import com.kokaihop.utility.CustomLinearLayoutManager;
import com.kokaihop.utility.RecyclerViewScrollListener;
import com.kokaihop.utility.SharedPrefUtils;

public class MyCookbooksFragment extends Fragment {

    private CookbooksAdapter adapter;
    private MyCookbooksViewModel viewModel;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FragmentCookbookLoginBinding binding;

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
        String userId = SharedPrefUtils.getSharedPrefStringData(getActivity(), Constants.USER_ID);
        String userFriendlyUrl = SharedPrefUtils.getSharedPrefStringData(getActivity(), Constants.FRIENDLY_URL);
        viewModel = new MyCookbooksViewModel(this, getContext(), userId, userFriendlyUrl);
        if (myCookbook && (accessToken == null) || accessToken.isEmpty()) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbook_login, container, false);
            binding.setViewModel(viewModel);
            return binding.getRoot();
        } else {
            final FragmentMyCookbooksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_cookbooks, container, false);
            binding.setViewModel(viewModel);
            adapter = new CookbooksAdapter(CookbooksList.getCookbooksList().getCookbooks(), myCookbook, User.getInstance(), userFriendlyUrl);
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

                @Override
                public void getScrolledState(RecyclerView recyclerView) {
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
        adapter.getItemCount();
    }

    public void refresh() {
        if (getContext() != null) {
            String accessToken = SharedPrefUtils.getSharedPrefStringData(getContext(), Constants.ACCESS_TOKEN);
            if (!((accessToken == null) || accessToken.isEmpty())) {
                viewModel.setDownloading(true);
                viewModel.getCookbooksOfUser(0);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        binding.parentLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_cookbook));
    }
}

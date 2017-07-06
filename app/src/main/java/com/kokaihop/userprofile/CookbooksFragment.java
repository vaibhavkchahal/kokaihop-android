package com.kokaihop.userprofile;

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

import java.util.ArrayList;

public class CookbooksFragment extends Fragment {

    private CookbooksAdapter adapter;
    private CookbooksViewModel viewModel;
    private CustomLinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    User user;

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
        String userId = "";
        String friendlyUrl = "";
        if (bundle != null) {
            userId = bundle.getString(Constants.USER_ID);
            friendlyUrl = bundle.getString(Constants.FRIENDLY_URL);
        }
        user = new User();
        viewModel = new CookbooksViewModel(this, getContext(), userId);
        FragmentCookbooksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cookbooks, container, false);
        adapter = new CookbooksAdapter(this, user.getCookbooks(), myCookbook, user, friendlyUrl);
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
        return binding.getRoot();
    }

    //    notify the adapter about the chage in data.
    public void showUserProfile(ArrayList<Cookbook> cookbooks) {
//        adapter = new CookbooksAdapter(this, viewModel.getUser().getCookbooks(), false);
//        recyclerView.setAdapter(adapter);
        user.getCookbooks().clear();
        user.getCookbooks().addAll(cookbooks);
        adapter.notifyDataSetChanged();
    }
}

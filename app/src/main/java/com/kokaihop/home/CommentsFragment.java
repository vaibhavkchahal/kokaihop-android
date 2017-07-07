package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCommentsBinding;
import com.kokaihop.utility.NetworkUtils;
import com.kokaihop.utility.RecyclerViewScrollListener;

public class CommentsFragment extends Fragment implements CommentsViewModel.CommentDatasetListener {

    private CommentsViewModel commentsViewModel;
    private FragmentCommentsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);
        commentsViewModel = new CommentsViewModel(this);
        binding.setViewModel(commentsViewModel);
        initializerecyclerView();
        initializePullToRefresh();
        return binding.getRoot();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            binding.swipeRefreshLayout.setRefreshing(true);
            binding.txtviewNewCommentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.rvRecipeComments.scrollToPosition(0);
                    v.setVisibility(View.GONE);
                }
            });
            fetchLatestComments();
        }
    }

    private void initializerecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = binding.rvRecipeComments;
        recyclerView.setLayoutManager(layoutManager);
        HomeCommentsRecyclerAdapter adapter = new HomeCommentsRecyclerAdapter(commentsViewModel.getCommentsList());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                if (commentsViewModel.getOffset() + commentsViewModel.getMax() <= commentsViewModel.getTotalCommentCount()) {
                    commentsViewModel.fetchCommentFromServer(commentsViewModel.getOffset() + commentsViewModel.getMax(), 0, true);
                }
            }

            @Override
            public void getScrolledState(RecyclerView recyclerView) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    binding.txtviewNewCommentView.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initializePullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLatestComments();
            }
        });
    }

    void fetchLatestComments() {
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            binding.swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), R.string.check_intenet_connection, Toast.LENGTH_SHORT).show();
        }
        String afterDateCreated = commentsViewModel.getCommentsList().get(0).getDateCreated();
        int offset = 0;
        binding.swipeRefreshLayout.setEnabled(true);
        commentsViewModel.fetchCommentFromServer(offset, Long.valueOf(afterDateCreated), false);
    }

    @Override
    public void onUpdateCommentsList(int itemCount) {
        RecyclerView recyclerView = binding.rvRecipeComments;
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager.findFirstVisibleItemPosition() > 0 && binding.swipeRefreshLayout.isRefreshing() && itemCount > 0) {
                binding.txtviewNewCommentView.setVisibility(View.VISIBLE);
            } else {
                binding.txtviewNewCommentView.setVisibility(View.GONE);
            }
            binding.swipeRefreshLayout.setRefreshing(false);
//            recyclerView.scrollToPosition(0);
        }
    }
}

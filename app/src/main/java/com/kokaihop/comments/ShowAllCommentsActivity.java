package com.kokaihop.comments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityShowAllCommentsBinding;
import com.kokaihop.analytics.GoogleAnalyticsHelper;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.NetworkUtils;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class ShowAllCommentsActivity extends BaseActivity implements ShowCommentsViewModel.CommentDatasetListener {

    private ShowCommentsViewModel showCommentsViewModel;
    private ActivityShowAllCommentsBinding binding;
    private String comingFrom = "commentsSection";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_all_comments);
        String recipeID = getIntent().getStringExtra("recipeId");
        showCommentsViewModel = new ShowCommentsViewModel(recipeID, this);
        binding.setViewModel(showCommentsViewModel);
        initializeRecycleView();
        initializePullToRefresh();
        GoogleAnalyticsHelper.trackScreenName(getString(R.string.recipe_comment_screen));


    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.getViewModel().updateComments();
    }

    private void initializePullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtils.isNetworkConnected(ShowAllCommentsActivity.this)) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ShowAllCommentsActivity.this, R.string.check_intenet_connection, Toast.LENGTH_SHORT).show();
                }
                int max = showCommentsViewModel.getMax();
                int offset = showCommentsViewModel.getOffset() + showCommentsViewModel.getMax();
                if (offset < showCommentsViewModel.getTotalCommentCount()) {
                    binding.swipeRefreshLayout.setEnabled(true);
                    showCommentsViewModel.fetchCommentFromServer(offset, max, false);
                    showCommentsViewModel.setOffset(offset);
                } else {
                    binding.swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    private void initializeRecycleView() {
        RecyclerView recyclerView = binding.recyclerViewCommentsList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        ShowCommentRecyclerAdapter recyclerAdapter = new ShowCommentRecyclerAdapter(comingFrom, showCommentsViewModel.getCommentsList());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onUpdateCommentsList() {
        RecyclerView recyclerView = binding.recyclerViewCommentsList;
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
            binding.swipeRefreshLayout.setRefreshing(false);
            recyclerView.scrollToPosition(0);
        }
    }
}

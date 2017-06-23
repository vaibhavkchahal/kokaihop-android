package com.kokaihop.comments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityReplyCommentBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.NetworkUtils;

/**
 * Created by Vaibhav Chahal on 14/6/17.
 */

public class ReplyCommentActivity extends BaseActivity implements ReplyCommentViewModel.CommentDatasetListener {

    private ReplyCommentViewModel replyCommentViewModel;
    private ActivityReplyCommentBinding binding;
    private String comingFrom = "replySection";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reply_comment);
        String commentId = getIntent().getStringExtra("commentId");
        String recipeId = getIntent().getStringExtra("recipeId");
        replyCommentViewModel = new ReplyCommentViewModel(recipeId, commentId, this);
        binding.setViewModel(replyCommentViewModel);
        initializeRecycleView();
        initializePullToRefresh();
    }

    private void initializeRecycleView() {
        RecyclerView recyclerView = binding.recyclerViewCommentsList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ShowCommentRecyclerAdapter recyclerAdapter = new ShowCommentRecyclerAdapter(comingFrom, replyCommentViewModel.getCommentsList());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void initializePullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkUtils.isNetworkConnected(ReplyCommentActivity.this)) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ReplyCommentActivity.this, R.string.check_intenet_connection, Toast.LENGTH_SHORT).show();
                }
                binding.swipeRefreshLayout.setEnabled(true);
                replyCommentViewModel.fetchCommentFromServer(false);
            }
        });
    }

    @Override
    public void onUpdateComment() {
        RecyclerView recyclerView = binding.recyclerViewCommentsList;
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
            binding.swipeRefreshLayout.setRefreshing(false);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        }
    }
}

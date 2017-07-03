package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCommentsBinding;
import com.kokaihop.utility.RecyclerViewScrollListener;

public class CommentsFragment extends Fragment {

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
        commentsViewModel = new CommentsViewModel(binding.rvRecipeComments);
        binding.setViewModel(commentsViewModel);
        initializerecyclerView();
        return binding.getRoot();
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
                    commentsViewModel.fetchCommentFromServer(commentsViewModel.getOffset() + commentsViewModel.getMax(), true);
                }
            }
        });
    }
}

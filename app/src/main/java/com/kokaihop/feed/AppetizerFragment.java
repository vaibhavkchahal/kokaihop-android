package com.kokaihop.feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentAppetizerBinding;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.FeedRecyclerScrollListener;
import com.kokaihop.utility.SpacingItemDecoration;

import static android.databinding.DataBindingUtil.inflate;

public class AppetizerFragment extends Fragment {


    private FragmentAppetizerBinding fragmentAppetizerBinding;
    private RecipeFeedViewModel apeetizerViewModel;

    public AppetizerFragment() {
    }

    public static AppetizerFragment newInstance() {
        AppetizerFragment fragment = new AppetizerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAppetizerBinding = inflate(LayoutInflater.from(getActivity()), R.layout.fragment_appetizer, container, false);
        apeetizerViewModel = new RecipeFeedViewModel(getContext(), ApiConstants.BadgeType.APPETIZER_OF_THE_DAY);
        fragmentAppetizerBinding.setViewModel(apeetizerViewModel);
        initializeRecycleView();
        View rootView = fragmentAppetizerBinding.getRoot();
        return rootView;
    }

    private void initializeRecycleView() {
        RecyclerView rvAppetizer = fragmentAppetizerBinding.rvAppetizer;
        FeedRecyclerListingOperation feedRecyclerListingOperation = new FeedRecyclerListingOperation(apeetizerViewModel, rvAppetizer, ApiConstants.BadgeType.APPETIZER_OF_THE_DAY);
        int spacingInPixels = rvAppetizer.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvAppetizer.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        FeedRecyclerScrollListener scrollListener = feedRecyclerListingOperation.prepareFeedRecyclerView();
        rvAppetizer.addOnScrollListener(scrollListener);
    }
    
}

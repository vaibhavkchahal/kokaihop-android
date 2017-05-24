package com.kokaihop.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentVegetarianBinding;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.EndlessScrollListener;
import com.kokaihop.utility.SpacingItemDecoration;

import static android.databinding.DataBindingUtil.inflate;


public class VegetarianFragment extends Fragment {

    FragmentVegetarianBinding vegetarianBinding;
    private RecipeFeedViewModel vegetarianViewModel;

    public VegetarianFragment() {
        // Required empty public constructor
    }

    public static VegetarianFragment newInstance() {
        VegetarianFragment fragment = new VegetarianFragment();
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
        vegetarianBinding = inflate(LayoutInflater.from(getActivity()), R.layout.fragment_vegetarian, container, false);
        vegetarianViewModel = new RecipeFeedViewModel(getContext(), ApiConstants.BadgeType.VEGETARIAN_OF_THE_DAY);
        vegetarianBinding.setViewModel(vegetarianViewModel);
        initializeRecycleView();
        View rootView = vegetarianBinding.getRoot();
        return rootView;
    }

    private void initializeRecycleView() {
        RecyclerView rvVegetarian = vegetarianBinding.rvVegetarian;
        FeedRecyclerListingOperation feedRecyclerListingOperation = new FeedRecyclerListingOperation(vegetarianViewModel, rvVegetarian, ApiConstants.BadgeType.VEGETARIAN_OF_THE_DAY);
        int spacingInPixels = rvVegetarian.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvVegetarian.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        EndlessScrollListener scrollListener = feedRecyclerListingOperation.prepareFeedRecyclerView();
        rvVegetarian.addOnScrollListener(scrollListener);
    }

}

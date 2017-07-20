package com.kokaihop.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentVegetarianBinding;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.FeedRecyclerScrollListener;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SpacingItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static android.databinding.DataBindingUtil.inflate;


public class VegetarianFragment extends Fragment {

    FragmentVegetarianBinding vegetarianBinding;
    private RecipeFeedViewModel vegetarianViewModel;
    private RecyclerView rvVegetarian;
    private FeedRecyclerListingOperation feedRecyclerListingOperation;

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
        rvVegetarian = vegetarianBinding.rvVegetarian;
        feedRecyclerListingOperation = new FeedRecyclerListingOperation(vegetarianViewModel, rvVegetarian, ApiConstants.BadgeType.VEGETARIAN_OF_THE_DAY);
        int spacingInPixels = rvVegetarian.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvVegetarian.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        feedRecyclerListingOperation.prepareFeedRecyclerView();
        FeedRecyclerScrollListener scrollListener = feedRecyclerListingOperation.getScrollListener();
        rvVegetarian.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Subscribe(sticky = true)
    public void onEvent(RecipeRealmObject recipe) {
        if (getUserVisibleHint()) {
            Logger.e("Event bus Appetizer", "Event bus Appetizer");

            GridLayoutManager gridLayoutManager = feedRecyclerListingOperation.getLayoutManager();
            List<Object> recipeListWithAds = vegetarianViewModel.getRecipeListWithAdds();
            AppUtility appUtility = new AppUtility();
            appUtility.updateRecipeItemView(recipe, gridLayoutManager, rvVegetarian, recipeListWithAds);
            EventBus.getDefault().removeStickyEvent(recipe);
        }

    }
}

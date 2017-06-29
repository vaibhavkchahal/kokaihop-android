package com.kokaihop.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentDessertBinding;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.FeedRecyclerScrollListener;
import com.kokaihop.utility.SpacingItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static android.databinding.DataBindingUtil.inflate;


public class DessertFragment extends Fragment {
    private FragmentDessertBinding dessertBinding;
    private RecipeFeedViewModel desertViewModel;

    public DessertFragment() {
        // Required empty public constructor
    }


    public static DessertFragment newInstance() {
        DessertFragment fragment = new DessertFragment();
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
        dessertBinding = inflate(LayoutInflater.from(getActivity()), R.layout.fragment_dessert, container, false);
        desertViewModel = new RecipeFeedViewModel(getContext(), ApiConstants.BadgeType.DESSERT_OF_THE_DAY);
        dessertBinding.setViewModel(desertViewModel);
        initializeRecycleView();
        View rootView = dessertBinding.getRoot();
        return rootView;
    }

    private void initializeRecycleView() {
        RecyclerView rvDesert = dessertBinding.rvDesert;
        FeedRecyclerListingOperation feedRecyclerListingOperation = new FeedRecyclerListingOperation(desertViewModel, rvDesert, ApiConstants.BadgeType.DESSERT_OF_THE_DAY);
        int spacingInPixels = rvDesert.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvDesert.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        feedRecyclerListingOperation.prepareFeedRecyclerView();
        FeedRecyclerScrollListener scrollListener = feedRecyclerListingOperation.getScrollListener();
        rvDesert.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(RecipeDetailPostEvent recipeDetailPostEvent) {
        int recipePosition = recipeDetailPostEvent.getPosition();
        Recipe recipe = recipeDetailPostEvent.getRecipe();
        Object object = desertViewModel.getRecipeListWithAdds().get(recipePosition);
        if (object instanceof Recipe) {
            Recipe recipeObject = (Recipe) object;
            recipeObject.setFavorite(recipe.isFavorite());
            recipeObject.setLikes(recipe.getLikes());
            dessertBinding.rvDesert.getAdapter().notifyDataSetChanged();
        }
        EventBus.getDefault().removeAllStickyEvents();
    }

}

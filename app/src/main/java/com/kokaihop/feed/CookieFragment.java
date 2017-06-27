package com.kokaihop.feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCookieBinding;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.FeedRecyclerScrollListener;
import com.kokaihop.utility.SpacingItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static android.databinding.DataBindingUtil.inflate;


public class CookieFragment extends Fragment {

    private FragmentCookieBinding cookieBinding;
    private RecipeFeedViewModel cookieViewModel;

    public CookieFragment() {
        // Required empty public constructor
    }

    public static CookieFragment newInstance() {
        CookieFragment fragment = new CookieFragment();
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
        cookieBinding = inflate(LayoutInflater.from(getActivity()), R.layout.fragment_cookie, container, false);
        cookieViewModel = new RecipeFeedViewModel(getContext(), ApiConstants.BadgeType.COOKIE_OF_THE_DAY);
        cookieBinding.setViewModel(cookieViewModel);
        initializeRecycleView();
        View rootView = cookieBinding.getRoot();
        return rootView;

    }

    private void initializeRecycleView() {
        RecyclerView rvCookie = cookieBinding.rvCookie;
        FeedRecyclerListingOperation feedRecyclerListingOperation = new FeedRecyclerListingOperation(cookieViewModel, rvCookie, ApiConstants.BadgeType.COOKIE_OF_THE_DAY);
        int spacingInPixels = rvCookie.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvCookie.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        feedRecyclerListingOperation.prepareFeedRecyclerView();
        FeedRecyclerScrollListener scrollListener = feedRecyclerListingOperation.getScrollListener();
        rvCookie.addOnScrollListener(scrollListener);
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
        Object object = cookieViewModel.getRecipeListWithAdds().get(recipePosition);
        if (object instanceof Recipe) {
            Recipe recipeObject = (Recipe) object;
            recipeObject.setFavorite(recipe.isFavorite());
            recipeObject.setLikes(recipe.getLikes());
            cookieBinding.rvCookie.getAdapter().notifyDataSetChanged();
        }
    }

}

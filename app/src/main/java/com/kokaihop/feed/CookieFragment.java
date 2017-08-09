package com.kokaihop.feed;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentCookieBinding;
import com.kokaihop.database.RecipeRealmObject;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.AppCredentials;
import com.kokaihop.utility.AppUtility;
import com.kokaihop.utility.FeedRecyclerScrollListener;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SpacingItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static android.databinding.DataBindingUtil.inflate;


public class CookieFragment extends Fragment {

    private FragmentCookieBinding cookieBinding;
    private RecipeFeedViewModel cookieViewModel;
    private FeedRecyclerListingOperation feedRecyclerListingOperation;
    private RecyclerView rvCookie;

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
        rvCookie = cookieBinding.rvCookie;
        feedRecyclerListingOperation = new FeedRecyclerListingOperation(cookieViewModel, rvCookie, ApiConstants.BadgeType.COOKIE_OF_THE_DAY);
        int spacingInPixels = rvCookie.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvCookie.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        feedRecyclerListingOperation.prepareFeedRecyclerView();
        FeedRecyclerScrollListener scrollListener = feedRecyclerListingOperation.getScrollListener();
        rvCookie.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
            Logger.e("Event bus Cookie", "Event bus Cookie");
            GridLayoutManager gridLayoutManager = feedRecyclerListingOperation.getLayoutManager();
            List<Object> recipeListWithAds = cookieViewModel.getRecipeListWithAdds();
            AppUtility appUtility = new AppUtility();
            appUtility.updateRecipeItemView(recipe, gridLayoutManager, rvCookie, recipeListWithAds);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AppUtility appUtility = new AppUtility();
            cookieViewModel.getRecipeListWithAdds().clear();
            cookieViewModel.getRecipeListWithAdds().addAll(cookieViewModel.getRecipeList());
            appUtility.addAdvtInRecipeList(cookieViewModel.getRecipeListWithAdds(), AppCredentials.DAILY_ADS_UNIT_IDS, getContext());
            feedRecyclerListingOperation.prepareFeedRecyclerView();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            AppUtility appUtility = new AppUtility();
            cookieViewModel.getRecipeListWithAdds().clear();
            cookieViewModel.getRecipeListWithAdds().addAll(cookieViewModel.getRecipeList());
            appUtility.addAdvtInRecipeList(cookieViewModel.getRecipeListWithAdds(), AppCredentials.DAILY_ADS_UNIT_IDS, getContext());
            feedRecyclerListingOperation.prepareFeedRecyclerView();
        }
    }

}

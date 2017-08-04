package com.kokaihop.feed;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentMainCourseBinding;
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


public class MainCourseFragment extends Fragment {

    private static MainCourseFragment fragment;
    private FragmentMainCourseBinding mainCourseBinding;
    private RecipeFeedViewModel mainCourseViewModel;
    private FeedRecyclerListingOperation feedRecyclerListingOperation;
    private RecyclerView rvMainCourse;
    private int numOfColumnInGrid;
    private int spanSizeForItemRecipe = 1;


    public MainCourseFragment() {
        // Required empty public constructor
    }

    public static MainCourseFragment newInstance() {
        fragment = new MainCourseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle sasvedInstanceState) {
        // Inflate the layout for this fragment
        mainCourseBinding = inflate(LayoutInflater.from(getActivity()), R.layout.fragment_main_course, container, false);
        mainCourseViewModel = new RecipeFeedViewModel(getContext(), ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);
        mainCourseBinding.setViewModel(mainCourseViewModel);
        initializeRecycleView();
        View rootView = mainCourseBinding.getRoot();
        return rootView;
    }

    private void initializeRecycleView() {
        rvMainCourse = mainCourseBinding.rvMainCourse;
        feedRecyclerListingOperation = new FeedRecyclerListingOperation(mainCourseViewModel, rvMainCourse, ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY);
        int spacingInPixels = rvMainCourse.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvMainCourse.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        feedRecyclerListingOperation.prepareFeedRecyclerView();
        FeedRecyclerScrollListener scrollListener = feedRecyclerListingOperation.getScrollListener();
        mainCourseBinding.rvMainCourse.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
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
            Logger.e("Event bus mainCourse", "Event bus mainCourse");
            GridLayoutManager gridLayoutManager = feedRecyclerListingOperation.getLayoutManager();
            List<Object> recipeListWithAds = mainCourseViewModel.getRecipeListWithAdds();
            AppUtility appUtility = new AppUtility();
            appUtility.updateRecipeItemView(recipe, gridLayoutManager, rvMainCourse, recipeListWithAds);
            EventBus.getDefault().removeStickyEvent(recipe);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("config changes", "landscape");
            AppUtility appUtility = new AppUtility();
            appUtility.addAdvtInRecipeList(mainCourseViewModel.getRecipeListWithAdds(), AppCredentials.DAILY_ADS_UNIT_IDS, getContext());
            numOfColumnInGrid = AppUtility.getColumnsAccToScreenSize();
            final FeedRecyclerAdapter recyclerAdapter = new FeedRecyclerAdapter(mainCourseViewModel.getRecipeListWithAdds(), numOfColumnInGrid);
            GridLayoutManager layoutManager = (GridLayoutManager) mainCourseBinding.rvMainCourse.getLayoutManager();
            layoutManager.setSpanCount(numOfColumnInGrid);
            layoutManager.setSpanSizeLookup
                    (new GridLayoutManager.SpanSizeLookup() {
                         @Override
                         public int getSpanSize(int position) {
                             switch (recyclerAdapter.getItemViewType(position)) {
                                 case FeedRecyclerAdapter.TYPE_ITEM_DAY_RECIPE:
                                     return numOfColumnInGrid;
                                 case FeedRecyclerAdapter.TYPE_ITEM_RECIPE:
                                     return spanSizeForItemRecipe;
                                 case FeedRecyclerAdapter.TYPE_ITEM_ADVT:
                                     return numOfColumnInGrid;
                                 default:
                                     return -1;
                             }
                         }
                     }
                    );
            mainCourseBinding.rvMainCourse.setLayoutManager(layoutManager);
            mainCourseBinding.rvMainCourse.setAdapter(recyclerAdapter);
            recyclerAdapter.notifyDataSetChanged();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getContext(), "portrait", Toast.LENGTH_SHORT).show();
            Log.i("config changes", "portrait");
            AppUtility appUtility = new AppUtility();
            appUtility.addAdvtInRecipeList(mainCourseViewModel.getRecipeListWithAdds(), AppCredentials.DAILY_ADS_UNIT_IDS, getContext());
            numOfColumnInGrid = AppUtility.getColumnsAccToScreenSize();
            final FeedRecyclerAdapter recyclerAdapter = new FeedRecyclerAdapter(mainCourseViewModel.getRecipeListWithAdds(), numOfColumnInGrid);
            GridLayoutManager layoutManager = (GridLayoutManager) mainCourseBinding.rvMainCourse.getLayoutManager();
            layoutManager.setSpanCount(numOfColumnInGrid);
            layoutManager.setSpanSizeLookup
                    (new GridLayoutManager.SpanSizeLookup() {
                         @Override
                         public int getSpanSize(int position) {
                             switch (recyclerAdapter.getItemViewType(position)) {
                                 case FeedRecyclerAdapter.TYPE_ITEM_DAY_RECIPE:
                                     return numOfColumnInGrid;
                                 case FeedRecyclerAdapter.TYPE_ITEM_RECIPE:
                                     return spanSizeForItemRecipe;
                                 case FeedRecyclerAdapter.TYPE_ITEM_ADVT:
                                     return numOfColumnInGrid;
                                 default:
                                     return -1;
                             }
                         }
                     }
                    );
            mainCourseBinding.rvMainCourse.setLayoutManager(layoutManager);
            mainCourseBinding.rvMainCourse.setAdapter(recyclerAdapter);
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}

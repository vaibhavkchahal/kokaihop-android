package com.kokaihop.feed.maincourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentMainCourseBinding;
import com.kokaihop.feed.FeedRecyclerListingOperation;
import com.kokaihop.feed.RecipeFeedViewModel;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.EndlessScrollListener;
import com.kokaihop.utility.SpacingItemDecoration;

import static android.databinding.DataBindingUtil.inflate;


public class MainCourseFragment extends Fragment {

    private FragmentMainCourseBinding mainCourseBinding;
    private RecipeFeedViewModel mainCourseViewModel;

    public MainCourseFragment() {
        // Required empty public constructor
    }

    public static MainCourseFragment newInstance() {
        MainCourseFragment fragment = new MainCourseFragment();
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
        mainCourseBinding = inflate(LayoutInflater.from(getActivity()), R.layout.fragment_main_course, container, false);
        mainCourseViewModel = new RecipeFeedViewModel(getContext(), ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY.name());
        mainCourseBinding.setViewModel(mainCourseViewModel);
        initializeRecycleView();
        View rootView = mainCourseBinding.getRoot();
        return rootView;
    }

    private void initializeRecycleView() {
        RecyclerView rvMainCourse = mainCourseBinding.rvMainCourse;
        FeedRecyclerListingOperation feedRecyclerListingOperation = new FeedRecyclerListingOperation(mainCourseViewModel, rvMainCourse, ApiConstants.BadgeType.MAIN_COURSE_OF_THE_DAY.name());
        int spacingInPixels = rvMainCourse.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvMainCourse.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        EndlessScrollListener scrollListener = feedRecyclerListingOperation.prepareFeedRecyclerView();
        mainCourseBinding.rvMainCourse.addOnScrollListener(scrollListener);
    }

}

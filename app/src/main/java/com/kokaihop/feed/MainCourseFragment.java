package com.kokaihop.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentMainCourseBinding;
import com.kokaihop.utility.EndlessScrollListener;
import com.kokaihop.utility.SpacingItemDecoration;

import static android.databinding.DataBindingUtil.inflate;


public class MainCourseFragment extends Fragment {

    private FragmentMainCourseBinding mainCourseBinding;
    private MainCourseViewModel mainCourseViewModel;

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
        mainCourseViewModel = new MainCourseViewModel(getContext());
        mainCourseBinding.setViewModel(mainCourseViewModel);
        initializeRecycleView();
        View rootView = mainCourseBinding.getRoot();
        return rootView;
    }

    private void initializeRecycleView() {
        RecyclerView rvFeed = mainCourseBinding.rvFeed;
        int spacingInPixels = getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvFeed.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        final FeedRecyclerAdapter recyclerAdapter = new FeedRecyclerAdapter(mainCourseViewModel.getRecipeListWithAdds());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (recyclerAdapter.getItemViewType(position)) {
                    case FeedRecyclerAdapter.TYPE_ITEM_DAY_RECIPE:
                        return 2;
                    case FeedRecyclerAdapter.TYPE_ITEM_RECIPE:
                        return 1;
                    case FeedRecyclerAdapter.TYPE_ITEM_ADVT:
                        return 2;
                    default:
                        return -1;
                }
            }
        });
        rvFeed.setLayoutManager(layoutManager);
        rvFeed.setAdapter(recyclerAdapter);
        ////////////////////////
        EndlessScrollListener scrollListener = new EndlessScrollListener(layoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!mainCourseViewModel.isLoadMore()) {
                    mainCourseViewModel.getRecipes(mainCourseViewModel.getOffset() + mainCourseViewModel.getMax(), true);
                    final FeedRecyclerAdapter adapter = (FeedRecyclerAdapter) mainCourseBinding.rvFeed.getAdapter();
                    final int curSize = adapter.getItemCount();
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRangeInserted(curSize, mainCourseViewModel.getRecipeListWithAdds().size() - 1);
                        }
                    });
                }
            }
        };
        mainCourseBinding.rvFeed.addOnScrollListener(scrollListener);
    }

}

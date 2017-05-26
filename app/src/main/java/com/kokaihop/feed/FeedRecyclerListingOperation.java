package com.kokaihop.feed;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.feed.maincourse.FeedRecyclerAdapter;
import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.EndlessScrollListener;
import com.kokaihop.utility.SpacingItemDecoration;

import static com.kokaihop.KokaihopApplication.getContext;

/**
 * Created by Vaibhav Chahal on 24/5/17.
 */

public class FeedRecyclerListingOperation {

    private GridLayoutManager layoutManager;
    private RecyclerView recyclerViewFeed;
    private RecipeFeedViewModel feedViewModel;
    private ApiConstants.BadgeType badgeType;

    public FeedRecyclerListingOperation(RecipeFeedViewModel feedViewModel, RecyclerView recyclerView, ApiConstants.BadgeType badgeType) {
        this.recyclerViewFeed = recyclerView;
        this.feedViewModel = feedViewModel;
        this.badgeType = badgeType;
    }

    public EndlessScrollListener prepareFeedRecyclerView() {
        int spacingInPixels = recyclerViewFeed.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        recyclerViewFeed.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        layoutManager = new GridLayoutManager(getContext(), 2);
        final FeedRecyclerAdapter recyclerAdapter = new FeedRecyclerAdapter(feedViewModel.getRecipeListWithAdds());
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
                                        }
        );
        recyclerViewFeed.setLayoutManager(layoutManager);
        recyclerViewFeed.setAdapter(recyclerAdapter);
        EndlessScrollListener scrollListener = new EndlessScrollListener(layoutManager) {

            @Override
            public void onLoadMore(RecyclerView view) {
                if (!feedViewModel.isDownloading() && feedViewModel.getOffset() + feedViewModel.getMax() < feedViewModel.MAX_BADGE) {
                    boolean showProgressDialog = true;
                    boolean isDownloading = true;

                    feedViewModel.getRecipes(feedViewModel.getOffset() + feedViewModel.getMax(), showProgressDialog, isDownloading, badgeType);
                    final FeedRecyclerAdapter adapter = (FeedRecyclerAdapter) view.getAdapter();
                    final int curSize = adapter.getItemCount();
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRangeInserted(curSize, feedViewModel.getRecipeListWithAdds().size() - 1);
                        }
                    });
                }
            }

            @Override
            public void onSyncDatabase(RecyclerView recyclerView, int lastVisibleItemPosition) {

                if (!feedViewModel.isDownloading()) {

                    if (lastVisibleItemPosition <= feedViewModel.getMax()) {
                        feedViewModel.setOffset(0);

                    } else if (lastVisibleItemPosition > feedViewModel.getMax() && lastVisibleItemPosition <= feedViewModel.getMax() * 2) {
                        feedViewModel.setOffset(feedViewModel.getMax());

                    } else if (lastVisibleItemPosition > feedViewModel.getMax() * 2) {
                        feedViewModel.setOffset(feedViewModel.getMax() * 2);


                    }
                    boolean showProgressDialog = false;
                    boolean isDownloading = true;
                    feedViewModel.getRecipes(feedViewModel.getOffset(), showProgressDialog, isDownloading, badgeType);
                    final FeedRecyclerAdapter adapter = (FeedRecyclerAdapter) recyclerView.getAdapter();
                    final int curSize = adapter.getItemCount();
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRangeInserted(curSize, feedViewModel.getRecipeListWithAdds().size() - 1);
                        }
                    });

                }
            }
        };
        return scrollListener;
    }

}

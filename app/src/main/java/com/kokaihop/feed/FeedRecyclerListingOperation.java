package com.kokaihop.feed;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kokaihop.utility.ApiConstants;
import com.kokaihop.utility.DateTimeUtils;
import com.kokaihop.utility.FeedRecyclerScrollListener;

import static com.kokaihop.KokaihopApplication.getContext;

/**
 * Created by Vaibhav Chahal on 24/5/17.
 */

public class FeedRecyclerListingOperation {

    private GridLayoutManager layoutManager;
    private RecyclerView recyclerViewFeed;
    private RecipeFeedViewModel feedViewModel;
    private ApiConstants.BadgeType badgeType;

    public FeedRecyclerListingOperation(RecipeFeedViewModel feedViewModel, RecyclerView recyclerView,
                                        ApiConstants.BadgeType badgeType) {
        this.recyclerViewFeed = recyclerView;
        this.feedViewModel = feedViewModel;
        this.badgeType = badgeType;
    }

    public void prepareFeedRecyclerView() {
//        int spacingInPixels = recyclerViewFeed.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
//        recyclerViewFeed.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
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

    }

    public  FeedRecyclerScrollListener getScrollListener()
    {
        FeedRecyclerScrollListener scrollListener = new FeedRecyclerScrollListener(layoutManager) {

            @Override
            public void onLoadMore(RecyclerView view) {
                if (!feedViewModel.isDownloading() && feedViewModel.getOffset() + feedViewModel.getMax() <= feedViewModel.MAX_BADGE) {
                    boolean showProgressDialog = true;
                    boolean isDownloading = true;
                    int max = feedViewModel.getMax();
                    int offset = feedViewModel.getOffset() + feedViewModel.getMax();
                    if (feedViewModel.getOffset() + feedViewModel.getMax() == 20) {
                        offset = feedViewModel.getOffset() + feedViewModel.getMax() + 1; // to get the 21th recipe
                    }
                    feedViewModel.getRecipes(offset, max, showProgressDialog, isDownloading, badgeType);
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
                Object object = feedViewModel.getRecipeListWithAdds().get(lastVisibleItemPosition);
                if (object instanceof AdvtDetail) {
                    object = feedViewModel.getRecipeListWithAdds().get(lastVisibleItemPosition - 1);
                }
                Recipe recipe = (Recipe) object;
                if (!feedViewModel.isDownloading() && DateTimeUtils.getOneHoursDiff(recipe.getLastUpdated()) >= 1) {
                    int max = feedViewModel.getMax();
                    if (lastVisibleItemPosition <= feedViewModel.getMax()) {
                        feedViewModel.setOffset(0);
                        max = feedViewModel.getMax() + 1; // to get the 21th recipe

                    } else if (lastVisibleItemPosition > feedViewModel.getMax() && lastVisibleItemPosition <= feedViewModel.getMax() * 2) {
                        feedViewModel.setOffset(feedViewModel.getMax() + 1);
                    } else if (lastVisibleItemPosition > feedViewModel.getMax() * 2) {
                        feedViewModel.setOffset(feedViewModel.getMax() * 2 + 1);
                    }
                    boolean showProgressDialog = false;
                    boolean isDownloading = true;
                    feedViewModel.getRecipes(feedViewModel.getOffset(), max, showProgressDialog, isDownloading, badgeType);
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

package com.kokaihop.search;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySearchBinding;
import com.altaworks.kokaihop.ui.databinding.DialogSearchFilterBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.database.SearchSuggestionRealmObject;
import com.kokaihop.feed.FeedRecyclerAdapter;
import com.kokaihop.search.SearchViewModel.DataSetListener;
import com.kokaihop.utility.HorizontalDividerItemDecoration;
import com.kokaihop.utility.Logger;
import com.kokaihop.utility.SpacingItemDecoration;

import java.lang.reflect.Field;
import java.util.List;

import static com.kokaihop.KokaihopApplication.getContext;


public class SearchActivity extends BaseActivity implements DataSetListener, SearchView.OnQueryTextListener {

    private ActivitySearchBinding binding;
    private SearchViewModel searchViewModel;
    private RecyclerView recyclerViewRecentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        initializeSearchView();
        initialiseSuggestionView();
        binding.included.linearlytNewlyAddedRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.included.linearlytNewlyAddedRecipe.setVisibility(View.GONE);
                binding.included.linearlytRecentSearch.setVisibility(View.GONE);
                newlyAddedRecipe();
            }
        });
        searchViewModel = new SearchViewModel(this);
        binding.setViewModel(searchViewModel);
        binding.imageviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void newlyAddedRecipe() {
        RecyclerView rvMainCourse = binding.included.rvRecipes;
        final FeedRecyclerAdapter recyclerAdapter = new FeedRecyclerAdapter(searchViewModel.fetchNewlyAddedRecipeWithAds());
        recyclerAdapter.setFromSearchedView(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
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

                                                    case FeedRecyclerAdapter.TYPE_ITEM_SEARCH_COUNT:
                                                        return 2;
                                                    default:
                                                        return -1;
                                                }
                                            }
                                        }
        );
        rvMainCourse.setLayoutManager(layoutManager);

        rvMainCourse.setAdapter(recyclerAdapter);


        int spacingInPixels = rvMainCourse.getContext().getResources().getDimensionPixelOffset(R.dimen.recycler_item_space);
        rvMainCourse.addItemDecoration(new SpacingItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
//        feedRecyclerListingOperation.prepareFeedRecyclerView();
    }

    private void initialiseSuggestionView() {
        recyclerViewRecentSearch = binding.included.recyclerviewRecentSearch;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewRecentSearch.setLayoutManager(layoutManager);
        recyclerViewRecentSearch.addItemDecoration(new HorizontalDividerItemDecoration(SearchActivity.this));
    }

    private void initializeSearchView() {

        TextView searchText = (TextView)
                binding.searchviewRecipe.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        binding.searchviewRecipe.onActionViewExpanded();
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Regular.ttf");
        searchText.setTypeface(myCustomFont);
        searchText.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.white));
        searchText.setHintTextColor(ContextCompat.getColor(SearchActivity.this, R.color.white));
        binding.searchviewRecipe.setQueryHint(getString(R.string.search_recipes_ingredents));

        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchText, R.drawable.cursor_white); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }
        binding.searchviewRecipe.setOnQueryTextListener(this);

    }

    private BottomSheetDialog setDialogConfigration(DialogSearchFilterBinding portionBinding) {
        BottomSheetDialog dialog = new BottomSheetDialog(SearchActivity.this);
        dialog.setContentView(portionBinding.getRoot());
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        //Grab the window of the portionDialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the portionDialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    @Override
    public void showFilterDialog(List<FilterData> filterDataList, String selectedFilter, final View view, String title) {
        DialogSearchFilterBinding binding = DataBindingUtil.
                inflate(LayoutInflater.from(SearchActivity.this), R.layout.dialog_search_filter, (ViewGroup) this.binding.getRoot(), false);
        final BottomSheetDialog filterDialog = setDialogConfigration(binding);
        binding.textviewTitle.setText(title);
        RecyclerView recylRecyclerViewFilter = binding.recyclerViewFilter;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recylRecyclerViewFilter.setLayoutManager(layoutManager);
        recylRecyclerViewFilter.addItemDecoration(new HorizontalDividerItemDecoration(SearchActivity.this));
        SearchFilterAdapter searchFilterAdapter = new SearchFilterAdapter(filterDataList, selectedFilter,
                new SearchFilterAdapter.FilterDataItemClickListener() {
                    @Override
                    public void onItemClick(FilterData filterData) {
                        if (view instanceof TextView) { //Filter selected.
                            TextView textView = (TextView) view;
                            if (filterData.getName().equals(getString(R.string.all))) {
                                textView.setBackgroundResource(R.drawable.search_tag_white);
                                textView.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.grey_FF8D929C));
                            } else {
                                textView.setBackgroundResource(R.drawable.search_tag_orange);
                                textView.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.white));
                            }
                            textView.setText(filterData.getName());
                        } else {
                            //SortBy selected
                            view.setTag(filterData.getName());
                        }
                        filterDialog.dismiss();
                    }
                });

        recylRecyclerViewFilter.setAdapter(searchFilterAdapter);
        binding.textviewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
            }
        });
        filterDialog.show();

    }

    @Override
    public void showWithImageDialog(View view, View parentView, String text, boolean selected) {
        if (selected) {

        } else {

        }

    }

    @Override
    public void updateSearchSuggestions(List<SearchSuggestionRealmObject> searchSuggestionList) {

        if (searchSuggestionList != null && searchSuggestionList.size() > 0) {
            binding.included.linearlytRecentSearch.setVisibility(View.VISIBLE);
            SearchSuggestionAdapter searchSuggestionAdapter = new SearchSuggestionAdapter(searchSuggestionList,
                    new SearchSuggestionAdapter.SuggestionDataItemClickListener() {
                        @Override
                        public void onItemClick(SearchSuggestionRealmObject searchSuggestionRealmObject) {
                            Logger.e("keyword", searchSuggestionRealmObject.getKeyword());
                        }
                    });
            recyclerViewRecentSearch.setAdapter(searchSuggestionAdapter);
        } else {
            binding.included.linearlytRecentSearch.setVisibility(View.GONE);

        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchViewModel.addSearchSuggestion(query);
        updateSearchSuggestions(searchViewModel.getSearchSuggestion());
        binding.searchviewRecipe.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

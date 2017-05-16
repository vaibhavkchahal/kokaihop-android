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

import static android.databinding.DataBindingUtil.inflate;


public class MainCourseFragment extends Fragment {

    private FragmentMainCourseBinding mainCourseBinding;
    private MainCourseViewModel mainCourseViewModel;
    private int offset = 0;

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
        mainCourseViewModel = new MainCourseViewModel();
        mainCourseBinding.setViewModel(mainCourseViewModel);
        initializeRecycleView();
        View rootView = mainCourseBinding.getRoot();
        return rootView;
    }

    private void initializeRecycleView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mainCourseBinding.rvFeed.setLayoutManager(layoutManager);
        mainCourseBinding.rvFeed.setAdapter(new RecipeRecyclerAdapter(mainCourseViewModel.getRecipeDetailsList()));
        mainCourseBinding.rvFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

//                Log.i("item no->",)

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && mainCourseViewModel.getRecipeCount() > mainCourseViewModel.getRecipeDetailsList().size()) {
                    mainCourseViewModel.getRecipes(mainCourseViewModel.getOffset() + mainCourseViewModel.getMax());
                    mainCourseBinding.rvFeed.getAdapter().notifyDataSetChanged();
                }

            }
        });
    }

}

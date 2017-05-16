package com.kokaihop.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.FragmentMainCourseBinding;

import static android.databinding.DataBindingUtil.inflate;


public class MainCourseFragment extends Fragment {

    private FragmentMainCourseBinding mainCourseBinding;

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
        mainCourseBinding = inflate(
                LayoutInflater.from(getActivity()),
                R.layout.fragment_main_course,
                container,
                false);

        MainCourseViewModel mainCourseViewModel = new MainCourseViewModel();
        mainCourseBinding.setViewModel(new MainCourseViewModel());
        View rootView = mainCourseBinding.getRoot();
        return rootView;
    }

}

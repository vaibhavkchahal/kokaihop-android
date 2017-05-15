package com.kokaihop.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;

public class CookbooksFragment extends Fragment {

    public CookbooksFragment() {
    }

    public static CookbooksFragment newInstance(String param1, String param2) {
        CookbooksFragment fragment = new CookbooksFragment();
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
        return inflater.inflate(R.layout.fragment_cookbooks, container, false);
    }
}

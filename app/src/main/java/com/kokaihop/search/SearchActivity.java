package com.kokaihop.search;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivitySearchBinding;
import com.kokaihop.base.BaseActivity;


public class SearchActivity extends BaseActivity {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        SearchViewModel searchViewModel = new SearchViewModel();
        binding.setViewModel(searchViewModel);
    }
}

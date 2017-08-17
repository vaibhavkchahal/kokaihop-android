package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityImageViewerBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.Constants;

import java.util.ArrayList;

public class ImageViewerActivity extends BaseActivity {


    private ActivityImageViewerBinding binding;
    private ArrayList<String> imageUrlList;
    private ImageViewerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewer);
        imageUrlList = getIntent().getStringArrayListExtra(Constants.IMAGE_URL_LIST);
        position = getIntent().getIntExtra(Constants.IMAGE_POSITION, 0);
        adapter = new ImageViewerAdapter(this,imageUrlList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.vpImageViewer.setAdapter(adapter);
        binding.vpImageViewer.setVerticalScrollbarPosition(position);
    }
}

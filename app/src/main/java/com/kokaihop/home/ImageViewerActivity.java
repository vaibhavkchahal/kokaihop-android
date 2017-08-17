package com.kokaihop.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityImageViewerBinding;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.Constants;

public class ImageViewerActivity extends BaseActivity {


    private ActivityImageViewerBinding binding;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewer);
        imageUrl = getIntent().getStringExtra(Constants.IMAGE_URL);
        binding.setImageUrl(imageUrl);
    }
}

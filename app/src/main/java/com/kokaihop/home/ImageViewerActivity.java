package com.kokaihop.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ActivityImageViewerBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.CameraUtils;
import com.kokaihop.utility.CloudinaryUtils;
import com.kokaihop.utility.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImageViewerActivity extends BaseActivity {


    private ActivityImageViewerBinding binding;
    private ArrayList<String> imageUrlList;
    private ImageViewerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int imagePosition;
    private Bitmap mBitmap;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewer);
        imageUrlList = getIntent().getStringArrayListExtra(Constants.IMAGE_URL_LIST);
        imagePosition = getIntent().getIntExtra(Constants.IMAGE_POSITION, 0);
        adapter = new ImageViewerAdapter(this, imageUrlList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        context = this;
        binding.vpImageViewer.setAdapter(adapter);
        binding.vpImageViewer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (imageUrlList.size() > 1)
                    binding.tvTitleImageViewer.setText(position + 1 + " " + getString(R.string.of) + " " + imageUrlList.size());
                imagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.vpImageViewer.setCurrentItem(imagePosition);
        if (imageUrlList.size() > 1)
            binding.tvTitleImageViewer.setText(imagePosition + 1 + " " + getString(R.string.of) + " " + imageUrlList.size());

        binding.ivBackImageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.ivShareImageViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getBaseContext())
                        .load(CloudinaryUtils.getFullImageUrl(imageUrlList.get(imagePosition)))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                mBitmap = resource;
                                shareImage();

                            }
                        });
            }
        });
    }

    public void shareImage() {
        Bitmap icon = mBitmap;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        if (CameraUtils.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File image = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_NAME + "/" + "temp.jpg");
            try {
                image.createNewFile();
                FileOutputStream fo = new FileOutputStream(image);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", image));
//        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
            startActivity(Intent.createChooser(share, "Share Image"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            shareImage();
        }
    }
}


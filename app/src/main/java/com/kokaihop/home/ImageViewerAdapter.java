package com.kokaihop.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.altaworks.kokaihop.ui.R;
import com.altaworks.kokaihop.ui.databinding.ItemImageViewerBinding;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 17/8/17.
 */

public class ImageViewerAdapter extends PagerAdapter {

    ArrayList<String> imageUrlList;
    ItemImageViewerBinding binding;
    private LayoutInflater mLayoutInflater;
    Context context;


    public ImageViewerAdapter(Context context, ArrayList<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_image_viewer, container, false);
        container.addView(binding.getRoot());
        binding.setImageUrl(imageUrlList.get(position));
        return binding.getRoot();
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

}

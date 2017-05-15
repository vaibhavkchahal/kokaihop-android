package com.kokaihop.feed;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseActivity;


public class FeedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}

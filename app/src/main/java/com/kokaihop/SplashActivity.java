package com.kokaihop;

import android.content.Intent;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.feed.FeedActivity;
import com.kokaihop.utility.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();
            }
        }, Constants.SPLASH_VISIBLE_TIME);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
    }
}

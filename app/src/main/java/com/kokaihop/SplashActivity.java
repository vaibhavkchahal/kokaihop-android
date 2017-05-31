package com.kokaihop;

import android.content.Intent;
import android.os.Bundle;

import com.altaworks.kokaihop.ui.R;
import com.crashlytics.android.Crashlytics;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.home.HomeActivity;
import com.kokaihop.utility.Constants;

import io.fabric.sdk.android.Fabric;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
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

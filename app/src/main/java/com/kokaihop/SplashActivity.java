package com.kokaihop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.altaworks.kokaihop.ui.R;
import com.kokaihop.authentication.login.LoginActivity;
import com.kokaihop.base.BaseActivity;
import com.kokaihop.utility.Constants;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, Constants.SPLASH_VISIBLE_TIME);
    }
}

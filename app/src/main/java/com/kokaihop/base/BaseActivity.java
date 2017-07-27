package com.kokaihop.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.batch.android.Batch;
import com.kokaihop.utility.Logger;

/**
 * Created by Vaibhav Chahal on 2/5/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Batch.onStart(this);
        Logger.e("installation id", Batch.User.getInstallationID() + "");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        Batch.onStop(this);

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Batch.onDestroy(this);

        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Batch.onNewIntent(this, intent);

        super.onNewIntent(intent);
    }
}

package com.kokaihop.customviews;

/**
 * Created by Rajendra Singh on 8/10/15.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hegazy on 2/13/15.
 */
public class NonSwipeableViewPager extends android.support.v4.view.ViewPager {
    private boolean enabled;

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public boolean getPagingEnabled() {
        return enabled;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}


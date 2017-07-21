package com.kokaihop.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Vaibhav Chahal on 20/7/17.
 */

public class CircularTextview extends android.support.v7.widget.AppCompatTextView {

    public CircularTextview(Context context) {
        super(context);
    }

    public CircularTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h = this.getMeasuredHeight();
        int w = this.getMeasuredWidth();
        int r = Math.max(w, h);
        setMeasuredDimension(r, r);
    }
}

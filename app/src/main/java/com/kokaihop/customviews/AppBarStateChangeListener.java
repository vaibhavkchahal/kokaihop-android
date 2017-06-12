package com.kokaihop.customviews;

import android.support.design.widget.AppBarLayout;

import com.kokaihop.utility.Logger;

/**
 * Created by Rajendra Singh on 8/6/17.
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        SCROLL_DOWN,
        IDLE
    }

    private State mCurrentState = State.IDLE;
    private int previousVerticalOffset=0;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        verticalOffset = Math.abs(verticalOffset);
        Logger.d("verticalOffset abs", verticalOffset + "");
        Logger.d("previousVerticalOffset", previousVerticalOffset + "");
        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        }
        else if (verticalOffset >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        }
        else if(previousVerticalOffset-2>verticalOffset)
        {
            if (mCurrentState != State.SCROLL_DOWN) {
                onStateChanged(appBarLayout, State.SCROLL_DOWN);
            }
            mCurrentState = State.SCROLL_DOWN;
        }
        else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
        previousVerticalOffset=verticalOffset;
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);


}


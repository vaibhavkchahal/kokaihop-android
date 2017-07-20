package com.kokaihop.home;

/**
 * Created by Vaibhav Chahal on 19/7/17.
 */

public class ShoppingListCounterEvent {

    private int count;

    public ShoppingListCounterEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

}

package com.kokaihop.home;

/**
 * Created by Vaibhav Chahal on 10/7/17.
 */

public class AuthUpdateEvent {

    String event;

    public AuthUpdateEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}

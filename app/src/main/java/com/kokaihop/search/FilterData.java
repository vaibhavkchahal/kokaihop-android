package com.kokaihop.search;

/**
 * Created by Rajendra Singh on 21/6/17.
 */

public class FilterData {

    private String name="";
    private String friendlyUrl="";
    private boolean isCurrentSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public boolean isCurrentSelected() {
        return isCurrentSelected;
    }

    public void setCurrentSelected(boolean currentSelected) {
        isCurrentSelected = currentSelected;
    }
}

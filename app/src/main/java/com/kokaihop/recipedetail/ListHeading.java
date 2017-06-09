package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 2/6/17.
 */

public class ListHeading {
    private String title;
    private long commentCount;

    public ListHeading(String title) {
        this.title = title;
    }

    public ListHeading(String title, long count) {
        this.title = title;
        this.commentCount = count;
    }

    public String getTitle() {
        return title;
    }

    public long getCommentCount() {
        return commentCount;
    }

}

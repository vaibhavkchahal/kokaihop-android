package com.kokaihop.recipe;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class RecipeRequestParams {


    private int fetchFacets, max, offset, withImages;
    private String timeStamp;

    public int getFetchFacets() {
        return fetchFacets;
    }

    public void setFetchFacets(int fetchFacets) {
        this.fetchFacets = fetchFacets;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getWithImages() {
        return withImages;
    }

    public void setWithImages(int withImages) {
        this.withImages = withImages;
    }

    public String getSortParams() {
        return sortParams;
    }

    public void setSortParams(String sortParams) {
        this.sortParams = sortParams;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String sortParams, type;


    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

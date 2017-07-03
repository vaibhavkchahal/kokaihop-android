package com.kokaihop.cookbooks.model;

import com.kokaihop.userprofile.model.Cookbook;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 30/6/17.
 */

public class CookbooksList {
    private ArrayList<Cookbook> cookbooks = new ArrayList<>();

    private static CookbooksList cookbooksList;

    public static CookbooksList getCookbooksList() {
        if(cookbooksList==null){
            cookbooksList = new CookbooksList();
        }
        return cookbooksList;
    }

    public ArrayList<Cookbook> getCookbooks() {
        return cookbooks;
    }

    public void setCookbooks(ArrayList<Cookbook> cookbooks) {
        this.cookbooks = cookbooks;
    }
}

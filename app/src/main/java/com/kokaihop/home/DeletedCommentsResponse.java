package com.kokaihop.home;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajendra Singh on 27/7/17.
 */

public class DeletedCommentsResponse {

    @SerializedName("comments")
    private ArrayList<DeletedComments> deletedComments;

    public ArrayList<DeletedComments> getDeletedComments() {
        return deletedComments;
    }

    public void setDeletedComments(ArrayList<DeletedComments> deletedComments) {
        this.deletedComments = deletedComments;
    }

    class DeletedComments {
        @SerializedName("_id")
        private String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }
}

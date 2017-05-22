package com.kokaihop.home.userprofile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajendra Singh on 22/5/17.
 */

public class Counter {

    @SerializedName("questions")
    private int questions;

    @SerializedName("answers")
    private int answers;

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }
}

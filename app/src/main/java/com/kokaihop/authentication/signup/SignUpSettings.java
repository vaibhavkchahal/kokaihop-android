package com.kokaihop.authentication.signup;

/**
 * Created by Vaibhav Chahal on 9/5/17.
 */

public class SignUpSettings {

    private int isNewsLetter;
    private int isSuggestionOfDay;
    private int noEmails;

    public SignUpSettings(int isNewsLetter,int isSuggestionOfDay) {
        this.isNewsLetter = isNewsLetter;
        this.isSuggestionOfDay = isSuggestionOfDay;
    }
}

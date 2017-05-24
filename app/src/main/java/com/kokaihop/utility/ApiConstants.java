package com.kokaihop.utility;

/**
 * Created by Rajendra Singh on 9/5/17.
 */

public class ApiConstants {

    public static final String MOST_RECENT = "most_recent";
    private String badgeType = "MAIN_COURSE_OF_THE_DAY";


    public enum BadgeType {
        MAIN_COURSE_OF_THE_DAY("mainCourseOfTheDay"),
        APPETIZER_OF_THE_DAY("appetizerOfTheDay"),
        COOKIE_OF_THE_DAY("cookieOfTheDay"),
        DESSERT_OF_THE_DAY("dessertOfTheDay"),
        VEGETARIAN_OF_THE_DAY("vegetarianOfTheDay");

        public String value;

        BadgeType(String value) {
            this.value = value;
        }

    }

    public enum RecipeType {Recipe}
}

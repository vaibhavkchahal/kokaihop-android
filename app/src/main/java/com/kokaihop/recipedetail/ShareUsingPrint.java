package com.kokaihop.recipedetail;

/**
 * Created by Vaibhav Chahal on 27/7/17.
 */

public class ShareUsingPrint {

    private String title;
    private int icon;
    private String packageName = "com.kokaihop.print";
    private String className = "print";
    private String ingredients;
    private String directions;
    private String recipeDescription;


    public ShareUsingPrint(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }
}

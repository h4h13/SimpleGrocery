package com.sharebuttons.grocery.grocery;

/**
 * Created by Monkey D Luffy on 7/15/2015.
 */
public class Grocery {

    private String mGrocery;
    private String mRate;

    private int mIcon;
    private String mTitle;
    private String mColor;

    public Grocery(String title, int icon, String color) {
        mIcon = icon;
        mTitle = title;
        mColor = color;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Grocery() {
    }

    public Grocery(String grocery, String rate, String color) {
        mRate = rate;
        mColor = color;
        mGrocery = grocery;
    }

    public String getGrocery() {
        return mGrocery;
    }

    public void setGrocery(String grocery) {
        mGrocery = grocery;
    }

    public String getRate() {
        return mRate;
    }

    public void setRate(String rate) {
        mRate = rate;
    }
}

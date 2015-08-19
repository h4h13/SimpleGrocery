package com.sharebuttons.grocery;

/**
 * Created by Monkey D Luffy on 8/4/2015.
 */
public class LowerToUp {
    public String firstLetterUpperCase(String item) {
        StringBuilder builder = new StringBuilder(item);
        builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
        return builder.toString();
    }
}

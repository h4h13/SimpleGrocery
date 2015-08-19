package com.sharebuttons.grocery.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Monkey D Luffy on 8/1/2015.
 */
public class GrocerySettings {

    SharedPreferences mSharedPreferences;

    public GrocerySettings(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getIsLaunched() {
        return mSharedPreferences.getBoolean("isLaunchedOne", false);
    }

    public void setIsLaunched(boolean trueOrFalse) {
        mSharedPreferences.edit().putBoolean("isLaunchedOne", trueOrFalse).apply();
    }

    public boolean getFromApp() {
        return mSharedPreferences.getBoolean("fromApp", false);
    }

    public void setFromApp(boolean trueOrFalse) {
        mSharedPreferences.edit().putBoolean("fromApp", trueOrFalse).apply();
    }

    public String getUserName() {
        return mSharedPreferences.getString("user_name", null);
    }

    public void setUserName(String userName) {
        mSharedPreferences.edit().putString("user_name", userName).apply();
    }
}

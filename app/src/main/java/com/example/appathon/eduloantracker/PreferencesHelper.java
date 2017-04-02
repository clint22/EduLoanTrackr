package com.example.appathon.eduloantracker;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static PreferencesHelper instance;
    private Context context;
    private SharedPreferences preferences;

    public PreferencesHelper(Context c) {
        this.context = c.getApplicationContext();
        preferences = context.getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);

    }

    public static PreferencesHelper getInstance(Context c) {
        if (instance == null) {
            instance = new PreferencesHelper(c);
        }
        return instance;
    }

    public void storeUnencryptedSetting(String key, String value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public String getUnencryptedSetting(String key) {
        return preferences.getString(key, "");
    }

}

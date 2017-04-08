package com.example.appathon.eduloantracker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Clint on 8/4/17.
 */

public class SharedPref {

    private static SharedPreferences prefs;
    private static String S_PREFS_NAME = "app_eduloantrackrapp_prefs";
    private static String S_PREFS_LOAN_ADDED = "loan_added";

    public static SharedPreferences getAppSharedPref(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(S_PREFS_NAME, Context.MODE_PRIVATE);
        }
        return prefs;
    }

    public static void setLoanAddedOrNot(Context context, Boolean loanaddornot) {

        prefs = getAppSharedPref(context);
        prefs.edit().putBoolean(S_PREFS_LOAN_ADDED, loanaddornot).apply();
    }

    public static Boolean getLoanAddedOrNot(Context context) {

        prefs = getAppSharedPref(context);
        return prefs.getBoolean(S_PREFS_LOAN_ADDED, false);
    }

}

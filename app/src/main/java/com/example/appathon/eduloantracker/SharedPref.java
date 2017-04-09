package com.example.appathon.eduloantracker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Clint on 8/4/17.
 */

public class SharedPref {

    private static SharedPreferences prefs;
    private static String S_PREFS_NAME = "app_eduloantrackrapp_prefs";
    private static String S_PREFS_INTEREST_RATE = "interest_rate";
    private static String S_PREFS_TOTAL_BALANCE = "total_balance";
    private static String S_PREFS_MONTHLY_PAY = "monthly_payment";
    private static String S_PREFS_TOTAL_LOAN = "loan_years";
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


    public static void setInterestRate(Context context, String interest) {
        prefs = getAppSharedPref(context);
        prefs.edit().putString(S_PREFS_INTEREST_RATE, interest).apply();
    }

    public static String getInterestRate(Context context) {
        prefs = getAppSharedPref(context);
        return prefs.getString(S_PREFS_INTEREST_RATE, "");
    }


    public static void setTotalBalance(Context context, String balance) {
        prefs = getAppSharedPref(context);
        prefs.edit().putString(S_PREFS_TOTAL_BALANCE, balance).apply();
    }

    public static String getTotalBalance(Context context) {
        prefs = getAppSharedPref(context);
        return prefs.getString(S_PREFS_TOTAL_BALANCE, "");
    }


    public static void setMonthlyPayment(Context context, String monthly) {
        prefs = getAppSharedPref(context);
        prefs.edit().putString(S_PREFS_MONTHLY_PAY, monthly).apply();
    }

    public static String getMonthlyPayment(Context context) {
        prefs = getAppSharedPref(context);
        return prefs.getString(S_PREFS_MONTHLY_PAY, "");
    }


    public static void setLoanTotal(Context context, String totalloan) {
        prefs = getAppSharedPref(context);
        prefs.edit().putString(S_PREFS_TOTAL_LOAN, totalloan).apply();
    }

    public static String getLoanTotal(Context context) {
        prefs = getAppSharedPref(context);
        return prefs.getString(S_PREFS_TOTAL_LOAN, "");
    }



}

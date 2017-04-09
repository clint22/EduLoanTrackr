package com.example.appathon.eduloantracker;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Clint on 8/4/17.
 */

public final class Utils {


    public static String getString(Object param) {
        try {
            if (param instanceof TextInputLayout) {
                TextInputLayout textInputLayout = (TextInputLayout) param;
                if (textInputLayout.getEditText() != null) {
                    return (textInputLayout.getEditText().getText().toString().trim());
                }
            }
            if (param instanceof EditText) return ((EditText) param).getText().toString().trim();
            if (param instanceof TextView) return ((TextView) param).getText().toString().trim();
            if (param instanceof String) return ((String) param).trim();
            if (param instanceof Editable) return param.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Boolean isNotEmpty(Object param) {
        try {
            String field = getString(param);
            return !(field.trim().isEmpty() || field.trim().equals("") || field.equals("null"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}

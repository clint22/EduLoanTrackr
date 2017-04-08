package com.example.appathon.eduloantracker;

import android.widget.EditText;

/**
 * Created by Clint on 8/4/17.
 */

public class Validation {

    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {

            editText.setError("This field cannot be empty");
            editText.requestFocus();
            return false;
        }

        return true;
    }
}

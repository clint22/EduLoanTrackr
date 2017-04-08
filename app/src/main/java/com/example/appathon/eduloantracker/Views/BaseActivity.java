package com.example.appathon.eduloantracker.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.appathon.eduloantracker.R;
import com.example.appathon.eduloantracker.Utils;

/**
 * Created by Clint on 8/4/17.
 */

public class BaseActivity extends AppCompatActivity {


    public Toolbar toolbar;


    public void setToolBar(String title) {

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(Utils.getString(title));
                setSupportActionBar(toolbar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setToolBarWithHomeEnabled(String title) {

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(Utils.getString(title));
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void goToActivity(Context context, Class destination) {
        startActivity(new Intent(context, destination));
    }

}

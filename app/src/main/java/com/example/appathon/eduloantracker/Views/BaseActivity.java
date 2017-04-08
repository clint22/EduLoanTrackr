package com.example.appathon.eduloantracker.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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


    protected static double calculateMonthlyPayment( int loanAmount, int tenure, double interestRate ) {


        // Convert interest rate into a decimal
        // eg. 6.5% = 0.065

        interestRate /= 100.0;

        // Monthly interest rate
        // is the yearly rate divided by 12

        double monthlyRate = interestRate / 12.0;

        // The length of the term in months
        // is the number of years times 12

        float termInMonths = tenure * 12;

        // Calculate the monthly payment
        // Typically this formula is provided so
        // we won't go into the details

        // The Math.pow() method is used calculate values raised to a power

        double monthlyPayment =
                (loanAmount*monthlyRate) /
                        (1-Math.pow(1+monthlyRate, -termInMonths));

        Log.e("monthlypaytwo", Utils.getString(monthlyPayment));
        return monthlyPayment;

    }

}

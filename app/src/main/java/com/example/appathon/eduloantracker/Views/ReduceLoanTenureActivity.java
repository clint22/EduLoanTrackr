package com.example.appathon.eduloantracker.Views;

import android.os.Bundle;

import com.example.appathon.eduloantracker.R;

public class ReduceLoanTenureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reduce_loan_tenure);
        setToolBarWithHomeEnabled("Reduce your Tenure");

    }
}

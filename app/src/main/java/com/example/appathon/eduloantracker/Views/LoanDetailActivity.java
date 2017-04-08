package com.example.appathon.eduloantracker.Views;

import android.os.Bundle;

import com.example.appathon.eduloantracker.R;

public class LoanDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);
        setToolBar("Loan Details");

    }
}

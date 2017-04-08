package com.example.appathon.eduloantracker.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.appathon.eduloantracker.Constants;
import com.example.appathon.eduloantracker.PreferencesHelper;
import com.example.appathon.eduloantracker.R;
import com.example.appathon.eduloantracker.Utils;
import com.example.appathon.eduloantracker.Validation;
import com.example.appathon.eduloantracker.model.LoanModel;
import com.example.appathon.eduloantracker.service.BankInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoanDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.edtxt_loan_bal)
    EditText edtxt_loan_bal;
    @BindView(R.id.edtxt_interest_rate)
    EditText edtxt_interest_rate;
    @BindView(R.id.edtxt_tenure)
    EditText edtxt_tenure;
    @BindView(R.id.lin_loan_balance)
    LinearLayout lin_loan_balance;
    @BindView(R.id.buttonGenerate)
    Button btnGenerate;

    private String outstandingAmt, rateOfInterest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);
        setToolBar("Loan Details");
        ButterKnife.bind(this);
        getLoanDetails();
        setClicks();

    }

    private void setClicks() {

        btnGenerate.setOnClickListener(this);
    }

    private void getLoanDetails() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pocketsapi.mybluemix.net/rest/Loan/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        String token = PreferencesHelper.getInstance(LoanDetailActivity.this).getUnencryptedSetting(Constants.TOKEN_PREF_KEY);
        String accno = PreferencesHelper.getInstance(LoanDetailActivity.this).getUnencryptedSetting(Constants.LOAN_ACNO_PREF_KEY);
        Call<List<LoanModel>> call = request.getLoanDetails(accno, token);
        call.enqueue(new Callback<List<LoanModel>>() {
            @Override
            public void onResponse(Call<List<LoanModel>> call, Response<List<LoanModel>> response) {
/*
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                List<LoanModel> loanModels = response.body();
                outstandingAmt = loanModels.get(1).getPrincipal_outstanding();
                rateOfInterest = loanModels.get(1).getRate_of_interest();
                PreferencesHelper.getInstance(LoanDetailActivity.this).storeUnencryptedSetting(Constants.OUT_AMT_KEY, outstandingAmt);

                lin_loan_balance.setVisibility(View.VISIBLE);

                if (loanModels.get(1).getPrincipal_outstanding() != null) {

                    edtxt_loan_bal.setText(outstandingAmt);
                }

                if (loanModels.get(1).getRate_of_interest() != null) {

                    edtxt_interest_rate.setText(rateOfInterest);
                }


            }

            @Override
            public void onFailure(Call<List<LoanModel>> call, Throwable t) {
                showToast("No Internet Connection");
            }


        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonGenerate:

                if (Validation.hasText(edtxt_tenure)) {
                    Intent intent = new Intent();
                    intent.putExtra("out_amount", outstandingAmt);
                    intent.putExtra("interest", rateOfInterest);
                    intent.putExtra("tenure", Utils.getString(edtxt_tenure));
                    setResult(RESULT_OK, intent);
                    finish();

                }

                break;
        }
    }
}

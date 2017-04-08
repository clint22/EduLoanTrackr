package com.example.appathon.eduloantracker.Views;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appathon.eduloantracker.Constants;
import com.example.appathon.eduloantracker.PreferencesHelper;
import com.example.appathon.eduloantracker.R;
import com.example.appathon.eduloantracker.UriList;
import com.example.appathon.eduloantracker.UserSessionManager;
import com.example.appathon.eduloantracker.model.AccountBalance;
import com.example.appathon.eduloantracker.model.AccountsModel;
import com.example.appathon.eduloantracker.model.AuthModel;
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

public class LandingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.txt_balance)
    TextView txt_balance;
    @BindView(R.id.buttonLogin)
    Button btn_login;

    private int progressStatus = 0;
    private Handler handler = new Handler();
    UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        setToolBar("EduLoanTrackr");
        ButterKnife.bind(this);
        session = new UserSessionManager(getApplicationContext());
        if (session.checkLogin())
            finish();


        loadJSON();
        loadAccount();
        setClicks();


    }

    private void setClicks() {

        btn_login.setOnClickListener(this);
    }


    private void loadJSON() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriList.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        Call<List<AuthModel>> call = request.getToken();
        call.enqueue(new Callback<List<AuthModel>>() {
            @Override
            public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
                List<AuthModel> authModel = response.body();
                String token = authModel.get(0).getToken();
                PreferencesHelper.getInstance(LandingActivity.this).storeUnencryptedSetting(Constants.TOKEN_PREF_KEY, token);
            }

            @Override
            public void onFailure(Call<List<AuthModel>> call, Throwable t) {
                showToast("No Internet Connection");
            }


        });

    }


    private void loadAccount() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://retailbanking.mybluemix.net/banking/icicibank/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        Call<List<AccountsModel>> call = request.getAccount();
        call.enqueue(new Callback<List<AccountsModel>>() {
            @Override
            public void onResponse(Call<List<AccountsModel>> call, Response<List<AccountsModel>> response) {
                List<AccountsModel> accountsModels = response.body();
                String accNo = accountsModels.get(0).getAccountNo();
                String loanAc = accountsModels.get(0).getLoanAccountNo();
                PreferencesHelper.getInstance(LandingActivity.this).storeUnencryptedSetting(Constants.ACNO_PREF_KEY, accNo);
                PreferencesHelper.getInstance(LandingActivity.this).storeUnencryptedSetting(Constants.LOAN_ACNO_PREF_KEY, loanAc);
                Log.e("retro", accNo);
                myAccount();

            }

            @Override
            public void onFailure(Call<List<AccountsModel>> call, Throwable t) {
                showToast("Please check your Internet Connection");
            }


        });


    }


    private void myAccount() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://retailbanking.mybluemix.net/banking/icicibank/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        String token = PreferencesHelper.getInstance(LandingActivity.this).getUnencryptedSetting(Constants.TOKEN_PREF_KEY);
        String accno = PreferencesHelper.getInstance(LandingActivity.this).getUnencryptedSetting(Constants.ACNO_PREF_KEY);
        Call<List<AccountBalance>> call = request.getMyAcc(token, accno);
        call.enqueue(new Callback<List<AccountBalance>>() {
            @Override
            public void onResponse(Call<List<AccountBalance>> call, Response<List<AccountBalance>> response) {
/*
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                List<AccountBalance> accountBalance = response.body();
                String accNo = accountBalance.get(1).getBalance();
                txt_balance.setText(accNo);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<AccountBalance>> call, Throwable t) {
            /*    swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                showToast("No Internet Connection");
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                session.logoutUser();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonLogin:
                getLoanDetails();
                break;
        }
    }

    private void getLoanDetails() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pocketsapi.mybluemix.net/rest/Loan/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        String token = PreferencesHelper.getInstance(LandingActivity.this).getUnencryptedSetting(Constants.TOKEN_PREF_KEY);
        String accno = PreferencesHelper.getInstance(LandingActivity.this).getUnencryptedSetting(Constants.LOAN_ACNO_PREF_KEY);
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
                String outstandingAmt = loanModels.get(1).getPrincipal_outstanding();
                String rateOfInterest = loanModels.get(1).getRate_of_interest();
                PreferencesHelper.getInstance(LandingActivity.this).storeUnencryptedSetting(Constants.OUT_AMT_KEY, outstandingAmt);
                Log.e("loandet", rateOfInterest);
                goToActivity(LandingActivity.this, LoanDetailActivity.class);
            }

            @Override
            public void onFailure(Call<List<LoanModel>> call, Throwable t) {
            /*    swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                showToast("No Internet Connection");
            }


        });


    }
}

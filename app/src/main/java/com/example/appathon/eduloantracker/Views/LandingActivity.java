package com.example.appathon.eduloantracker.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appathon.eduloantracker.Constants;
import com.example.appathon.eduloantracker.PreferencesHelper;
import com.example.appathon.eduloantracker.R;
import com.example.appathon.eduloantracker.SharedPref;
import com.example.appathon.eduloantracker.UriList;
import com.example.appathon.eduloantracker.UserSessionManager;
import com.example.appathon.eduloantracker.Utils;
import com.example.appathon.eduloantracker.model.AccountBalance;
import com.example.appathon.eduloantracker.model.AccountsModel;
import com.example.appathon.eduloantracker.model.AuthModel;
import com.example.appathon.eduloantracker.service.BankInterface;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LandingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.txt_balance)
    TextView txt_balance;
    @BindView(R.id.buttonLogin)
    Button btn_login;
    @BindView(R.id.rel_sync_loans)
    RelativeLayout rel_sync_loans;
    @BindView(R.id.rel_loan_dashboard)
    RelativeLayout rel_loan_dashboard;
    @BindView(R.id.btnPayLoan)
    Button btnPayLoan;
    @BindView(R.id.txt_total_loan)
    TextView txt_total_loan;
    @BindView(R.id.txt_interest_rate)
    TextView txt_interest_rate;
    @BindView(R.id.txt_total_balance)
    TextView txt_total_balance;
    @BindView(R.id.txt_total_monthly_pay)
    TextView txt_total_monthly_pay;
    @BindView(R.id.pb)
    ProgressBar progressBar;
    @BindView(R.id.perc_loan)
    TextView txt_perc_loan;
    @BindView(R.id.progressbar)
    ProgressBar progressBartwo;
    @BindView(R.id.rel_horizontal_bar)
    RelativeLayout rel_horizontal_bar;
    @BindView(R.id.paid_off)
    TextView txt_paid_off;
    @BindView(R.id.total_perc_loan)
    TextView txt_total_perc_loan;
    @BindView(R.id.txt_loan_desc)
    TextView txt_loan_desc;
    @BindView(R.id.rel_loan_details)
    RelativeLayout rel_loan_details;
   /* @BindView(R.id.txt_emi)
    TextView txt_last_three;
    @BindView(R.id.txt_last_three_emi)
    TextView txt_last_three_emi;
    @BindView(R.id.txt_emi_nos)
    TextView txt_emi_nos;
    @BindView(R.id.txt_total_emi)
    TextView txt_total_emi;
    @BindView(R.id.progressbar_analysis)
    ProgressBar progressBar2;*/

    private int loanAmount, tenure;
    private double interestRate;
    private double tot_balance, tot;
    private Boolean loanAddedOrNot = true;
    UserSessionManager session;
    private String loan_amt, loan_tenure, loan_ior, loanNo, loanAg, loanOut, token;
    private LineChart chart;
    private int progressStatus = 0;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        setToolBar("EduLoanTrackr");
        ButterKnife.bind(this);
        session = new UserSessionManager(getApplicationContext());
//        chart = (LineChart) findViewById(R.id.chart);
        if (session.checkLogin())
            finish();

        if (SharedPref.getLoanAddedOrNot(LandingActivity.this)) {

            setLoanDetails();


        } else {


            rel_sync_loans.setVisibility(View.VISIBLE);
        }


        loadJSON();
        setClicks();


    }

    private void setLoanDetails() {

       /* rel_loan_dashboard.setVisibility(View.VISIBLE);
        rel_horizontal_bar.setVisibility(View.VISIBLE);*/

        rel_loan_details.setVisibility(View.VISIBLE);


        txt_interest_rate.setText(SharedPref.getInterestRate(LandingActivity.this) + "%");
        txt_total_balance.setText(getString(R.string.total_balance, SharedPref.getTotalBalance(LandingActivity.this)));
        txt_total_monthly_pay.setText(getString(R.string.monthly_pay, SharedPref.getMonthlyPayment(LandingActivity.this)));
        txt_total_loan.setText("1");


        txt_total_perc_loan.setText(getString(R.string.tot_perc_loan, "500000"));


        if (txt_balance != null && txt_total_balance != null) {

            tot = 500000 - Double.parseDouble(SharedPref.getTotalBalance(LandingActivity.this));

            txt_paid_off.setText(getString(R.string.paid_off, Math.round(tot)));

        }

        if (txt_paid_off != null) {


            double tot_perc = tot / 500000;
            double tot_perc_two = tot_perc * 100;
            txt_perc_loan.setText(Math.round(tot_perc_two) + "%");
            progressBar.setProgress((int) Math.round(tot_perc_two));
        }

        txt_loan_desc.setText(Html.fromHtml(getString(R.string.desc_loan, SharedPref.getTotalBalance(LandingActivity.this),
                SharedPref.getInterestRate(LandingActivity.this) + "%", SharedPref.getMonthlyPayment(LandingActivity.this)
        )));


    }

    private void setClicks() {

        btn_login.setOnClickListener(this);
        btnPayLoan.setOnClickListener(this);
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
                loadAccount();
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
                tot_balance = Double.parseDouble(Utils.getString(txt_balance));
                Log.e("totbal", String.valueOf(tot_balance));
                progressBartwo.setVisibility(View.GONE);
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
                Intent intent = new Intent(LandingActivity.this, LoanDetailActivity.class);
                startActivityForResult(intent, Constants.LOAN_VERIFY);
                break;
            case R.id.btnPayLoan:
                Intent intent1 = new Intent(LandingActivity.this, AnalyseLoanActivity.class);
                intent1.putExtra("loanBalance", SharedPref.getTotalBalance(LandingActivity.this));
                intent1.putExtra("monthlyPay", SharedPref.getMonthlyPayment(LandingActivity.this));
                intent1.putExtra("noPayment", SharedPref.getLoanTenure(LandingActivity.this));
                intent1.putExtra("interestrate", SharedPref.getInterestRate(LandingActivity.this));
                startActivity(intent1);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (resultCode != RESULT_OK) return;
            switch (requestCode) {

                case Constants.LOAN_VERIFY:
                    if (data == null || data.getExtras() == null) return;
                    if (Utils.isNotEmpty(data.getExtras().getString("out_amount")) && Utils.isNotEmpty(data.getExtras().getString("interest"))
                            && Utils.isNotEmpty(data.getExtras().getString("tenure"))) {

                        showToast("Contains Key");
                        rel_sync_loans.setVisibility(View.GONE);
                        rel_loan_details.setVisibility(View.VISIBLE);
                        SharedPref.setLoanAddedOrNot(LandingActivity.this, true);

                        loan_amt = data.getExtras().getString("out_amount");
                        loan_tenure = data.getExtras().getString("tenure");
                        loan_ior = data.getExtras().getString("interest");
                        loan_ior = loan_ior.replace("%", "");
                        calcLoan();
                        calcLoanPercent();

                        Log.e("loandets", Utils.getString(loan_amt));


                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcLoanPercent() {

        txt_total_perc_loan.setText(getString(R.string.tot_perc_loan, "500000"));


        if (txt_balance != null && txt_total_balance != null) {

            tot = 500000 - Double.parseDouble(SharedPref.getTotalBalance(LandingActivity.this));

            txt_paid_off.setText(getString(R.string.paid_off, Math.round(tot)));

        }

        if (txt_paid_off != null) {


            double tot_perc = tot / 500000;
            double tot_perc_two = tot_perc * 100;
            txt_perc_loan.setText(Math.round(tot_perc_two) + "%");
            progressBar.setProgress((int) Math.round(tot_perc_two));
        }
    }

    private void calcLoan() {

        loanAmount = Integer.parseInt(loan_amt);
        tenure = Integer.parseInt(loan_tenure);
        interestRate = Double.parseDouble(loan_ior);
        double monthly_pay = calculateMonthlyPayment(loanAmount, tenure, interestRate);
        float monthly_pay_round = Math.round(monthly_pay);
        Log.e("monthlypay", String.valueOf(monthly_pay_round));

        SharedPref.setInterestRate(LandingActivity.this, loan_ior);
        txt_interest_rate.setText(loan_ior);

        SharedPref.setTotalBalance(LandingActivity.this, loan_amt);
        txt_total_balance.setText(loan_amt);

        SharedPref.setMonthlyPayment(LandingActivity.this, String.valueOf(monthly_pay_round));
        txt_total_monthly_pay.setText(String.valueOf(monthly_pay_round));

        SharedPref.setLoanTenure(LandingActivity.this, loan_tenure);


        txt_total_loan.setText("1");


    }

    /*public void getDialog() {
        new BottomDialog.Builder(this)
                .setTitle("LOAN ADVISORY FLOW ")
                .setContent("You can either choose to follow our payment plans or create one of your own manually")
                .setIcon(R.drawable.dollar)
                .setPositiveText("our plan")
                .setNegativeText("Generate Yourself ")
                .setNegativeTextColorResource(R.color.colorPrimary)
                .setPositiveBackgroundColorResource(R.color.colorPrimary)
                //.setPositiveBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                //.setPositiveTextColor(ContextCompat.getColor(this, android.R.color.colorPrimary)
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(BottomDialog dialog) {
                    getAnalysis();
                    }
                }).show();
    }

    private void getAnalysis() {
        progressBar2.setVisibility(View.VISIBLE);
        token=PreferencesHelper.getInstance(this).getUnencryptedSetting(Constants.TOKEN_PREF_KEY);
        loanAg=PreferencesHelper.getInstance(this).getUnencryptedSetting(Constants.AGMNT_ID);
        loanNo=PreferencesHelper.getInstance(this).getUnencryptedSetting(Constants.LOAN_NO);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://pocketsapi.mybluemix.net/rest/Loan/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            BankInterface request = retrofit.create(BankInterface.class);
            Call<List<EmiModel>> call=request.getEmi(loanNo,loanAg,token);
            call.enqueue(new Callback<List<EmiModel>>() {
                @Override
                public void onResponse(Call<List<EmiModel>> call, Response<List<EmiModel>> response) {
*//*
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                *//*
                    List<EmiModel> emiModels=response.body();
                    String lastThree=emiModels.get(1).getLastThreeEMIs();
                    String totalEmis=emiModels.get(1).getNoOfEMIs();
                    PreferencesHelper.getInstance(LandingActivity.this).storeUnencryptedSetting(Constants.LAST_THREE, lastThree);
                    txt_last_three.setText(lastThree);
                    txt_emi_nos.setText(totalEmis);
                    txt_last_three_emi.setVisibility(View.VISIBLE);
                    txt_total_emi.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.GONE);

                    List<Entry> entries = new ArrayList<Entry>();
                    entries.add(new Entry(2, 5000));
                    entries.add(new Entry(4, 5500));
                    entries.add(new Entry(9, 6000));
                    LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
                    dataSet.setColor(R.color.colorPrimary);
                    dataSet.setValueTextColor(R.color.white);
                    LineData lineData = new LineData(dataSet);
                    chart.setVisibility(View.VISIBLE);
                    chart.setData(lineData);
                    chart.invalidate(); // refresh
                }

                @Override
                public void onFailure(Call<List<EmiModel>> call, Throwable t) {
            *//*    swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                *//*  progressBar2.setVisibility(View.GONE);
                    Toast.makeText(LandingActivity.this,t.toString(), Toast.LENGTH_SHORT).show();
                }


            });


    }*/

}

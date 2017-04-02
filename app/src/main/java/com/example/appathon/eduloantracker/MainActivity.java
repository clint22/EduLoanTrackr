package com.example.appathon.eduloantracker;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appathon.eduloantracker.model.AccountBalance;
import com.example.appathon.eduloantracker.model.AccountsModel;
import com.example.appathon.eduloantracker.model.AuthModel;
import com.example.appathon.eduloantracker.model.LoanModel;
import com.example.appathon.eduloantracker.service.BankInterface;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textView,display,getAccount,account,balbt,baltxt,refresh,getLoan,loanTxt;
    private EditText email;
    private String URL="https://corporateapiprojectwar.mybluemix.net/corporate_banking/";
    private String outAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.button);
        getLoan=(TextView)findViewById(R.id.get_loan);
        loanTxt=(TextView)findViewById(R.id.loan_txt);
        refresh=(TextView)findViewById(R.id.refresh);
        getAccount=(TextView)findViewById(R.id.account);
        balbt=(TextView)findViewById(R.id.balance_bt);
        baltxt=(TextView)findViewById(R.id.balance_txt);
        display=(TextView)findViewById(R.id.status);
        account=(TextView)findViewById(R.id.acc);
        email=(EditText)findViewById(R.id.email);
        final LineChart chart = (LineChart) findViewById(R.id.chart);
       email.setVisibility(View.GONE);
        if (PreferencesHelper.getInstance(MainActivity.this).getUnencryptedSetting(Constants.OUT_AMT_KEY)!=null) {

            outAmt = PreferencesHelper.getInstance(MainActivity.this).getUnencryptedSetting(Constants.OUT_AMT_KEY);
        }else {
            outAmt ="100000";
        }
        float amt=Float.valueOf(outAmt);
        final float frac=amt/12;
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(2, 5000));
        entries.add(new Entry(4, 5500));
        entries.add(new Entry(9, 6000));
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(R.color.colorPrimary);
        dataSet.setValueTextColor(R.color.white);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh

  /*      swipeRefreshLayout =
                (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));
                */

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Entry> entries = new ArrayList<Entry>();
                for (int i=1;i<=12;i++) {
                    entries.add(new Entry(i, frac));
                }
                LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
                dataSet.setColor(R.color.colorPrimary);
                dataSet.setValueTextColor(R.color.white);
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate(); // refresh
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJSON();
            }
        });
        getAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAccount();
            }
        });
        balbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAc();
            }
        });
        getLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoanDetails();
            }
        });

/*
        mrecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mrecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mrecyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new  SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                      //  initViews();
                    }
                });
            }
        });
        */
    }

    private void getLoanDetails() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pocketsapi.mybluemix.net/rest/Loan/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        String token=PreferencesHelper.getInstance(MainActivity.this).getUnencryptedSetting(Constants.TOKEN_PREF_KEY);
        String accno=PreferencesHelper.getInstance(MainActivity.this).getUnencryptedSetting(Constants.LOAN_ACNO_PREF_KEY);
        Call<List<LoanModel>> call=request.getLoanDetails(accno,token);
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
                List<LoanModel> loanModels=response.body();
                String outstandingAmt=loanModels.get(1).getPrincipalOutstanding();
                PreferencesHelper.getInstance(MainActivity.this).storeUnencryptedSetting(Constants.OUT_AMT_KEY, outstandingAmt);
                loanTxt.setText(outstandingAmt);
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
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }


        });


    }

    private void MyAc() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://retailbanking.mybluemix.net/banking/icicibank/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        String token=PreferencesHelper.getInstance(MainActivity.this).getUnencryptedSetting(Constants.TOKEN_PREF_KEY);
        String accno=PreferencesHelper.getInstance(MainActivity.this).getUnencryptedSetting(Constants.ACNO_PREF_KEY);
        Call<List<AccountBalance>> call=request.getMyAcc(token,accno);
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
                List<AccountBalance> accountBalance=response.body();
                String accNo=accountBalance.get(1).getBalance();
                baltxt.setText(accNo);
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
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }


        });


    }

    private void loadAccount() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://retailbanking.mybluemix.net/banking/icicibank/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        Call<List<AccountsModel>> call=request.getAccount();
        call.enqueue(new Callback<List<AccountsModel>>() {
            @Override
            public void onResponse(Call<List<AccountsModel>> call, Response<List<AccountsModel>> response) {
/*
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                List<AccountsModel> accountsModels=response.body();
                String accNo=accountsModels.get(0).getAccountNo();
                String loanAc=accountsModels.get(0).getLoanAccountNo();
                account.setText(accNo);
                PreferencesHelper.getInstance(MainActivity.this).storeUnencryptedSetting(Constants.ACNO_PREF_KEY, accNo);
                PreferencesHelper.getInstance(MainActivity.this).storeUnencryptedSetting(Constants.LOAN_ACNO_PREF_KEY, loanAc);
            }

            @Override
            public void onFailure(Call<List<AccountsModel>> call, Throwable t) {
            /*    swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }


        });


    }


    private void loadJSON(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        Call<List<AuthModel>> call=request.getToken();
        call.enqueue(new Callback<List<AuthModel>>() {
            @Override
            public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
/*
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                List<AuthModel> authModel=response.body();
                String token=authModel.get(0).getToken();
                display.setText(authModel.get(0).getToken());
                PreferencesHelper.getInstance(MainActivity.this).storeUnencryptedSetting(Constants.TOKEN_PREF_KEY, token);
            }

            @Override
            public void onFailure(Call<List<AuthModel>> call, Throwable t) {
            /*    swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
            }


        });

    }




}

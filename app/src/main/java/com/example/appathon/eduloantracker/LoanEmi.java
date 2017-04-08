package com.example.appathon.eduloantracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appathon.eduloantracker.model.EmiModel;
import com.example.appathon.eduloantracker.model.LoanModel;
import com.example.appathon.eduloantracker.service.BankInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoanEmi extends AppCompatActivity {

    TextView loanTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_emi);

        loanTxt=(TextView)findViewById(R.id.last_three);

        loadLoanEmi();
    }

    private void loadLoanEmi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pocketsapi.mybluemix.net/rest/Loan/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankInterface request = retrofit.create(BankInterface.class);
        String token=PreferencesHelper.getInstance(LoanEmi.this).getUnencryptedSetting(Constants.TOKEN_PREF_KEY);
        String loanNo=PreferencesHelper.getInstance(LoanEmi.this).getUnencryptedSetting(Constants.LOAN_NO);
        String agreeId=PreferencesHelper.getInstance(LoanEmi.this).getUnencryptedSetting(Constants.AGMNT_ID);
        Call<List<EmiModel>> call=request.getEmi(loanNo,agreeId,token);
        call.enqueue(new Callback<List<EmiModel>>() {
            @Override
            public void onResponse(Call<List<EmiModel>> call, Response<List<EmiModel>> response) {
/*
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                List<EmiModel> emiModels=response.body();
                String lastThree=emiModels.get(1).getLastThreeEMIs();
                PreferencesHelper.getInstance(LoanEmi.this).storeUnencryptedSetting(Constants.LAST_THREE, lastThree);
                loanTxt.setText(lastThree);
            }

            @Override
            public void onFailure(Call<List<EmiModel>> call, Throwable t) {
            /*    swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                */
                Toast.makeText(LoanEmi.this,t.toString(),Toast.LENGTH_SHORT).show();
            }


        });
    }
}

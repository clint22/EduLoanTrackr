package com.example.appathon.eduloantracker.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appathon.eduloantracker.R;
import com.example.appathon.eduloantracker.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReduceLoanTenureActivity extends BaseActivity {


    @BindView(R.id.desc_loan_balance)
    TextView txt_loan_balance;
    @BindView(R.id.txt_current_desc_amount)
    TextView txt_principle_amount;
    @BindView(R.id.txt_current_interest_amount)
    TextView txt_interest_amount;
    @BindView(R.id.txt_current_total_amount)
    TextView txt_total_payment;
    @BindView(R.id.btnGo)
    ImageView btnGo;
    @BindView(R.id.txt_new_desc_amount)
    TextView txt_new_principle_amount;
    @BindView(R.id.txt_new_interest_amount)
    TextView txt_new_interest_amount;
    @BindView(R.id.txt_new_total_amount)
    TextView txt_new_total_amount;
    @BindView(R.id.rel_one)
    RelativeLayout cardViewOne;
    @BindView(R.id.edttxt_new_tenure)
    EditText edttxt_new_tenure;
    @BindView(R.id.txt_save_amount)
    TextView txt_save_amount;

    private String str_loan_balance, interest_rate;
    private Double str_principle_amount, str_interest_amount, str_total_payment, str_monthly_pay, str_no_of_payment, loan_bal ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reduce_loan_tenure);
        setToolBarWithHomeEnabled("Reduce your Tenure");
        ButterKnife.bind(this);
        setValues();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double no_of_payment = Double.parseDouble(Utils.getString(edttxt_new_tenure));

                Double monthly_pay = calculateMonthlyPayment(Integer.parseInt(str_loan_balance), Integer.parseInt(Utils.getString(edttxt_new_tenure)), Double.parseDouble(interest_rate));

                calcFormula(no_of_payment, monthly_pay);

            }
        });

    }

    private void calcFormula(Double str_no_of_payment, Double str_monthly_pay) {

        cardViewOne.setVisibility(View.VISIBLE);

        Double monthPay = (str_no_of_payment * 12);

        Log.e("accbal", String.valueOf(monthPay));

        Double AccruedAmount = str_monthly_pay * monthPay;
        Log.e("accbal", String.valueOf(AccruedAmount));

        loan_bal = Double.parseDouble(str_loan_balance);

        Double InterestAmount = AccruedAmount - loan_bal;
        Double PrincipleAmount = AccruedAmount - InterestAmount;

        Double yousave = str_total_payment - AccruedAmount;

        txt_new_total_amount.setText(getString(R.string.accrued_amount, Math.round(AccruedAmount)));
        txt_new_interest_amount.setText(getString(R.string.interest_amount, Math.round(InterestAmount)));
        txt_new_principle_amount.setText(getString(R.string.principle_amount, Math.round(PrincipleAmount)));
        txt_save_amount.setText(getString(R.string.principle_amount, Math.round(yousave)));


    }


    private void setValues() {

        str_loan_balance = getIntent().getExtras().getString("loan_balance");
        str_principle_amount = getIntent().getExtras().getDouble("principle_amount");
        str_interest_amount = getIntent().getExtras().getDouble("interest_amount");
        str_total_payment = getIntent().getExtras().getDouble("total_payment");
        str_monthly_pay = getIntent().getExtras().getDouble("monthly_pay");
        str_no_of_payment = getIntent().getExtras().getDouble("noOfPay");
        interest_rate = getIntent().getExtras().getString("interestrate");
        Log.d("int_rate", String.valueOf(interest_rate));

        if (str_loan_balance != null) {

            txt_loan_balance.setText(getString(R.string.loan_balance, str_loan_balance));


        }

        if (str_principle_amount != null) {

            txt_principle_amount.setText(getString(R.string.accrued_amount, Math.round(str_principle_amount)));
        }

        if (str_interest_amount != null) {

            txt_interest_amount.setText(getString(R.string.accrued_amount, Math.round(str_interest_amount)));
        }
        if (str_total_payment != null) {

            txt_total_payment.setText(getString(R.string.accrued_amount, Math.round(str_total_payment)));
        }
    }
}

package com.example.appathon.eduloantracker.Views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appathon.eduloantracker.PreferencesHelper;
import com.example.appathon.eduloantracker.R;
import com.example.appathon.eduloantracker.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.appathon.eduloantracker.R.id.txt_loan_analyse_bal;

public class AnalyseLoanActivity extends BaseActivity {

    @BindView(R.id.txt_total_payment)
    TextView txt_total_payment;
    @BindView(R.id.txt_loan_interest_amount)
    TextView txt_interest_amount;
    @BindView(R.id.txt_principle_amount)
    TextView txt_principle_amount;
    @BindView(txt_loan_analyse_bal)
    TextView txt_loan_balance;
    @BindView(R.id.btnReduceLoan)
    Button btnReduceLoan;

    private PieChart chart;
    private String loanBalance;
    private String MonthlyPayment;
    private String noOfPayment="5", interest_rate;
    private Double loanBal, MonthlyPay, AccruedAmount, PrincipleAmount, InterestAmount, TotalPayment, numberOfPayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_loan);
        setToolBarWithHomeEnabled("Analyse Your Loan");
        ButterKnife.bind(this);

        chart = (PieChart) findViewById(R.id.chart);

        setValues();

        btnReduceLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showReduceLoanDialog();
            }
        });


    }

    private void showReduceLoanDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_reduce_loan_dialog);
        dialog.setTitle("Reduce Your Loan");


        Button btnTwo = ButterKnife.findById(dialog, R.id.btntwo);
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AnalyseLoanActivity.this, ReduceLoanTenureActivity.class);
                intent.putExtra("loan_balance", loanBalance);
                intent.putExtra("principle_amount", PrincipleAmount);
                intent.putExtra("interest_amount", InterestAmount);
                intent.putExtra("total_payment", AccruedAmount);
                intent.putExtra("monthly_pay", MonthlyPay);
                intent.putExtra("noOfPay", numberOfPayment);
                intent.putExtra("interestrate", interest_rate);
                startActivity(intent);
                finish();
            }
        });


        dialog.show();


    }

    private void setValues() {

        loanBalance = getIntent().getExtras().getString("loanBalance");
        MonthlyPayment = getIntent().getExtras().getString("monthlyPay");
        noOfPayment = PreferencesHelper.getInstance(AnalyseLoanActivity.this).getUnencryptedSetting("tenure");
        interest_rate = getIntent().getExtras().getString("interestrate");
        Log.e("int_rate", Utils.getString(interest_rate));

        if (loanBalance != null) {

            txt_loan_balance.setText(getString(R.string.loan_balance, loanBalance));
        }

        if (noOfPayment != null && MonthlyPayment != null) {

            loanBal = Double.parseDouble(loanBalance);
            MonthlyPay = Double.parseDouble(MonthlyPayment);
            numberOfPayment = Double.parseDouble(noOfPayment);


            calcFormula(numberOfPayment, MonthlyPay);
        }
    }

    private void calcFormula(Double numberOfPayment, Double monthlyPay) {

        Double monthPay = (numberOfPayment * 12);

        Log.e("accbal", String.valueOf(monthPay));

        AccruedAmount = monthlyPay * monthPay;
        Log.e("accbal", String.valueOf(AccruedAmount));

        InterestAmount = AccruedAmount - loanBal;
        PrincipleAmount = AccruedAmount - InterestAmount;

        txt_total_payment.setText(getString(R.string.accrued_amount, Math.round(AccruedAmount)));
        txt_interest_amount.setText(getString(R.string.interest_amount, Math.round(InterestAmount)));
        txt_principle_amount.setText(getString(R.string.principle_amount, Math.round(PrincipleAmount)));

        if (txt_total_payment != null && txt_interest_amount != null && txt_principle_amount != null) {

            setChart(InterestAmount, PrincipleAmount);
        }


    }


    private void setChart(Double interestAmount, Double principleAmount) {


        chart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(principleAmount.floatValue(), "Principle Amount"));
        yvalues.add(new PieEntry(interestAmount.floatValue(), "Interest Amount"));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSet.setFormSize(25);
        dataSet.setValueTextSize(15);



       /* ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
        xVals.add("April");
        xVals.add("May");
        xVals.add("June");*/

        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        chart.setData(data);
        chart.getDescription().setText("");
        chart.animateXY(1000, 1000);
        chart.setEntryLabelColor(R.color.colorblack);
        chart.getDescription().setTextSize(15);


    }


}

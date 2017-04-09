package com.example.appathon.eduloantracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by navneet on 2/4/17.
 */

public class LoanModel {

    @SerializedName("principal_outstanding")
    private String principal_outstanding;


    @SerializedName("roi")
    private String rate_of_interest;

    @SerializedName("loan_no")
    @Expose
    private String loanNo;

    @SerializedName("agreementId")
    @Expose
    private String agreementId;

    public String getRate_of_interest() {
        return rate_of_interest;
    }

    public void setRate_of_interest(String rate_of_interest) {
        this.rate_of_interest = rate_of_interest;
    }

    public String getPrincipal_outstanding() {
        return principal_outstanding;
    }

    public void setPrincipal_outstanding(String principal_outstanding) {
        this.principal_outstanding = principal_outstanding;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public String getAgreementId() {
        return agreementId;
    }
}

package com.example.appathon.eduloantracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by navneet on 2/4/17.
 */

public class LoanModel {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("principal_outstanding")
    @Expose
    private String principalOutstanding;
    @SerializedName("date_of_loan")
    @Expose
    private String dateOfLoan;
    @SerializedName("type_of_loan")
    @Expose
    private String typeOfLoan;
    @SerializedName("roi")
    @Expose
    private String roi;
    @SerializedName("month_delinquency")
    @Expose
    private String monthDelinquency;
    @SerializedName("loanAmount")
    @Expose
    private String loanAmount;
    @SerializedName("custId")
    @Expose
    private String custId;
    @SerializedName("loan_no")
    @Expose
    private String loanNo;
    @SerializedName("agreementId")
    @Expose
    private String agreementId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getPrincipalOutstanding() {
        return principalOutstanding;
    }

    public void setPrincipalOutstanding(String principalOutstanding) {
        this.principalOutstanding = principalOutstanding;
    }

    public String getDateOfLoan() {
        return dateOfLoan;
    }

    public void setDateOfLoan(String dateOfLoan) {
        this.dateOfLoan = dateOfLoan;
    }

    public String getTypeOfLoan() {
        return typeOfLoan;
    }

    public void setTypeOfLoan(String typeOfLoan) {
        this.typeOfLoan = typeOfLoan;
    }

    public String getRoi() {
        return roi;
    }

    public void setRoi(String roi) {
        this.roi = roi;
    }

    public String getMonthDelinquency() {
        return monthDelinquency;
    }

    public void setMonthDelinquency(String monthDelinquency) {
        this.monthDelinquency = monthDelinquency;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

}

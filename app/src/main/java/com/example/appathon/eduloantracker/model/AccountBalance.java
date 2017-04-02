package com.example.appathon.eduloantracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by navneet on 1/4/17.
 * ############################
 */

public class AccountBalance {

        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("balance")
        @Expose
        private String balance;
        @SerializedName("accountno")
        @Expose
        private String accountno;
        @SerializedName("accounttype")
        @Expose
        private String accounttype;
        @SerializedName("balancetime")
        @Expose
        private String balancetime;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getAccountno() {
            return accountno;
        }

        public void setAccountno(String accountno) {
            this.accountno = accountno;
        }

        public String getAccounttype() {
            return accounttype;
        }

        public void setAccounttype(String accounttype) {
            this.accounttype = accounttype;
        }

        public String getBalancetime() {
            return balancetime;
        }

        public void setBalancetime(String balancetime) {
            this.balancetime = balancetime;
        }

}

package com.example.appathon.eduloantracker.service;

import com.example.appathon.eduloantracker.model.AccountBalance;
import com.example.appathon.eduloantracker.model.AccountsModel;
import com.example.appathon.eduloantracker.model.AuthModel;
import com.example.appathon.eduloantracker.model.EmiModel;
import com.example.appathon.eduloantracker.model.LoanModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by navneet on 29/3/17.
 */

public interface BankInterface {

    //Checking

    @GET("mybank/authenticate_client?client_id=krisnavneet.nk@gmail.com&password=U0HH66J9")
    Call<List<AuthModel>> getToken();

    @GET("participantmapping?client_id=krisnavneet.nk@gmail.com")
    Call<List<AccountsModel>> getAccount();

    /*   @GET("balanceenquiry?client_id=krisnavneet.nk@gmail.com&token={tokenNo}&accountno={accNo}")
       Call<List<AccountBalance>> getMyAcc(@Path("tokenNo") String token, @Path("accNo") String accno);
       */
    @GET("balanceenquiry?client_id=krisnavneet.nk@gmail.com")
    Call<List<AccountBalance>> getMyAcc(@Query("token") String token, @Query("accountno") String accno);

    @GET("getLoanDetails?clientId=krisnavneet.nk@gmail.comgetLoanDetails?clientId=krisnavneet.nk@gmail.com")
    Call<List<LoanModel>> getLoanDetails(@Query("param") String param, @Query("authToken") String token);

    @GET("EMIDetails?clientId=krisnavneet.nk@gmail.com")
    Call<List<EmiModel>> getEmi(@Query("loan_no") String loanNo, @Query("agreeID") String agreeId, @Query("authToken") String token);

}

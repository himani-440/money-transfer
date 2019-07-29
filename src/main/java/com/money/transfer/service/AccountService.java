package com.money.transfer.service;

import static spark.Spark.*;

import java.util.Date;

import com.google.gson.Gson;
import com.money.transfer.dao.AccountDao;
import com.money.transfer.dao.DBConnection;
import com.money.transfer.domain.Account;
import com.money.transfer.domain.StandardResponse;
import com.money.transfer.domain.StatusResponse;
import com.money.transfer.exception.AccountServiceException;

public class AccountService {

    public AccountDao accountDao;

    public AccountService() {
        try {
			accountDao = new AccountDao(DBConnection.getDSLContext());
		} catch (Exception e) {
			throw new AccountServiceException("500", e.getMessage());
		}
    }

    public  void getAccountService() {

        get("/accountDetails/:accountNum", (request, response) -> {
            final String accountNum = request.params(":accountNum");
            validateaccountNum(accountNum);
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, "200", new Gson()
                            .toJsonTree(accountDao.getAccountByAccNum(accountNum))));
        });


        get("/balance/:accountNum", (request, response) -> {
            final String accountNum = request.params(":accountNum");
            validateaccountNum(accountNum);
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, "200", new Gson()
                            .toJsonTree(accountDao.getAccountBalance(accountNum))));
        });



        put("/debit/:accountNum","application/json",(request, response) -> {
            final String accountNum = request.params(":accountNum");
            validateaccountNum(accountNum);
            Account account = new Gson().fromJson(request.body(), Account.class);
            accountDao.debitMoney(accountNum, account.getAmount());
            String successMsg = "Acct " + maskAcctNumber(accountNum) +" debited with INR " +account.getAmount()+ 
            		" on " +new Date();
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, "200", successMsg));
        });

        put("/credit/:accountNum", "application/json", (request, response) -> {
        	final String accountNum = request.params(":accountNum");
            validateaccountNum(accountNum);
            Account account = new Gson().fromJson(request.body(), Account.class);
            accountDao.creditMoney(accountNum, account.getAmount());
            String successMsg = "Acct " + maskAcctNumber(accountNum) +" credited with INR " +account.getAmount()+ 
            		" on " +new Date();
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, "200", successMsg));
        });

        put("/transfer/:fromAccount", (request, response) -> {
            final String fromAccNum = request.params("fromAccount");
            validateaccountNum(fromAccNum);
            Account account = new Gson().fromJson(request.body(), Account.class);
            final String toAccNum = account.getAccNumber();
            validateaccountNum(toAccNum);
            accountDao.transferMoney(fromAccNum, toAccNum, account.getAmount());
            String successMsg = "Acct " + maskAcctNumber(fromAccNum) +" debited with INR " +account.getAmount()+ 
            		" on " +new Date()+" & Acct " + maskAcctNumber(toAccNum)+" credited with INR " +account.getAmount();
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, "200", successMsg));
        });

        exception(AccountServiceException.class, (e, request, response) -> {
           response.type("application/json");
           response.body(new Gson()
                   .toJson(new StandardResponse(StatusResponse.ERROR, e.getResponseCode(), e.getResponseMsg())));
        });

    }
    
    private void validateaccountNum(String accountNum) {
    	String regex = "[0-9]+";
    	if(accountNum.isEmpty() || !accountNum.matches(regex) || accountNum.length()!=12){
    		throw new AccountServiceException("400", "Please provide valid Account Number");
    	}
	}

	private static String maskAcctNumber(String number) {
           return "xxxxxxxx"+number.substring(8);
     	}

}

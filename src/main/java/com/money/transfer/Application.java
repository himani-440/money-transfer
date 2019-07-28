package com.money.transfer;

import static spark.Spark.get;
import static spark.Spark.port;

import com.money.transfer.service.AccountService;

import spark.Spark;

public class Application {

    public static void main(String[] args) {
        port(8080);
        Spark.threadPool(10);
        Spark.after((req, res) -> res.type("application/json"));
       
        get("/hello", (request, response) -> "Hello!!");
        new AccountService().getAccountService();
    }
    
    public static void startServer(){
    	final String[] args = {};
    	Application.main(args);
        Spark.awaitInitialization();
    }
    
    public static void stopServer(){
    	Spark.stop();
        Spark.awaitStop();
    }
    
}

package com.money.transfer.domain;

import com.google.gson.JsonElement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StandardResponse {

    private StatusResponse status;
    private String statusCode;
    private String message;
    private JsonElement account;

    public StandardResponse(StatusResponse status, String statusCode, String message) {
        super();
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }

    public StandardResponse(StatusResponse status, String statusCode, JsonElement account) {
        super();
        this.status = status;
        this.statusCode = statusCode;
        this.account = account;
    }
}

package com.money.transfer.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountServiceException extends RuntimeException{

    private static final long serialVersionUID = 8898096792842500571L;

    private String responseCode;
    private String responseMsg;

    public AccountServiceException(String responseCode, String responseMsg) {
        super(responseMsg);
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }
}

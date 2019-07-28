package com.money.transfer.domain;

import lombok.Getter;

@Getter
public enum StatusResponse {

    SUCCESS,
    ERROR;
    private String status;
}

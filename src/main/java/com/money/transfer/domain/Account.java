package com.money.transfer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Account {

    private String accNumber;
    private String status;
    private Double amount;
    private Double balance;

}

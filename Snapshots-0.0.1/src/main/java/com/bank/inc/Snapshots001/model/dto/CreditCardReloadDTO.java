package com.bank.inc.Snapshots001.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreditCardReloadDTO {
    private String cardNumber;
    private BigDecimal balance;
}

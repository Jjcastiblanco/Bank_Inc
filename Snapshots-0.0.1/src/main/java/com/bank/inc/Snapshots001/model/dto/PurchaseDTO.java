package com.bank.inc.Snapshots001.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseDTO {

    private String cardNumber;
    private BigDecimal amount;
}

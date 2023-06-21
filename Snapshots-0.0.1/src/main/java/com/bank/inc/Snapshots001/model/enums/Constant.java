package com.bank.inc.Snapshots001.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Constant {
    FORMAT_CREDIT_CARD("%06d%010d"),
    REGEX_VALIDATE_CREDIT_CARD("[a-zA-Z]");

    private String value;

}

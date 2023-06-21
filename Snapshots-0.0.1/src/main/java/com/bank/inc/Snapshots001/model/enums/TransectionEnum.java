package com.bank.inc.Snapshots001.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransectionEnum {

    APPROVE("APPROVE"),
    REJECT("REJECT"),
    CANCEL("CANCEL");


    private String value;

}

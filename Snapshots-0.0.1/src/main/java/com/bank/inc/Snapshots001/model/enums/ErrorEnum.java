package com.bank.inc.Snapshots001.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    SUCCESS(0,"Success"),
    INCOMPLETE_NUMBERS(1,"Incomplete number");

    private int id;
    private String description;


}

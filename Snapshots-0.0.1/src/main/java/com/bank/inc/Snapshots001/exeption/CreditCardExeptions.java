package com.bank.inc.Snapshots001.exeption;

import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import org.springframework.stereotype.Component;

public class CreditCardExeptions extends Exception{
    public CreditCardExeptions(ResponseDTO messageError) {
        super(String.valueOf(messageError.getMessageError().getMessage()));
    }
}

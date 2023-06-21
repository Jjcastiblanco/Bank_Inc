package com.bank.inc.Snapshots001.model.dto;


import com.bank.inc.Snapshots001.model.MessageError;
import com.bank.inc.Snapshots001.model.entity.CreditCardEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO {

    private Object creditCardEntity;
    private MessageError messageError;
}

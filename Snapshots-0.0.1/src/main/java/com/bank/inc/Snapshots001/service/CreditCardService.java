package com.bank.inc.Snapshots001.service;

import com.bank.inc.Snapshots001.exeption.CreditCardExeptions;
import com.bank.inc.Snapshots001.model.dto.CreditCardReloadDTO;
import com.bank.inc.Snapshots001.model.dto.CreditCardRequestDTO;
import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;


public interface CreditCardService {

    ResponseEntity<ResponseDTO> generateCardNumber(String productId) throws CreditCardExeptions;
    ResponseEntity<ResponseDTO> activeCard(CreditCardRequestDTO cardRequestDTO, boolean activeCard) throws CreditCardExeptions;

    ResponseEntity<ResponseDTO> deleteCreditCard(CreditCardRequestDTO cardRequestDTO) throws CreditCardExeptions;
    ResponseEntity<ResponseDTO> reloadCard(CreditCardReloadDTO creditCardReloadDTO) throws CreditCardExeptions;

    ResponseEntity<ResponseDTO> getBalanceCard(CreditCardRequestDTO cardRequestDTO) throws CreditCardExeptions;
    void pay();

}

package com.bank.inc.Snapshots001.service;

import com.bank.inc.Snapshots001.model.dto.PurchaseDTO;
import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import com.bank.inc.Snapshots001.model.dto.TransactionCancelDTO;
import com.bank.inc.Snapshots001.model.dto.TransactionIdDTO;
import org.springframework.http.ResponseEntity;

public interface TransactionService {

    ResponseEntity<ResponseDTO> purchaseCreditCard(PurchaseDTO purchaseDTO);
    ResponseEntity<ResponseDTO> getPurchaseCreditCard(TransactionIdDTO transactionIdDTO);

    ResponseEntity<ResponseDTO> cancelPurchaseCreditCard(TransactionCancelDTO transactionCancelDTO);


}

package com.bank.inc.Snapshots001.controller;

import com.bank.inc.Snapshots001.model.MessageError;
import com.bank.inc.Snapshots001.model.dto.PurchaseDTO;
import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import com.bank.inc.Snapshots001.model.dto.TransactionCancelDTO;
import com.bank.inc.Snapshots001.model.dto.TransactionIdDTO;
import com.bank.inc.Snapshots001.model.entity.CreditCardEntity;
import com.bank.inc.Snapshots001.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {


    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<ResponseDTO> activateCreditCard(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            return transactionService.purchaseCreditCard(purchaseDTO);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }

    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ResponseDTO> createCardNumber(@PathVariable String transactionId)  {
        try {
            TransactionIdDTO transactionIdDTO = new TransactionIdDTO();
            transactionIdDTO.setTransactionId(transactionId);
            return transactionService.getPurchaseCreditCard(transactionIdDTO);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }

    }

    @PostMapping("/anulation")
    public ResponseEntity<ResponseDTO> anulationTransaction(@RequestBody TransactionCancelDTO transactionCancelDTO) {
        try {
            return transactionService.cancelPurchaseCreditCard(transactionCancelDTO);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }

    }
}

package com.bank.inc.Snapshots001.controller;

import com.bank.inc.Snapshots001.exeption.CreditCardExeptions;
import com.bank.inc.Snapshots001.model.MessageError;
import com.bank.inc.Snapshots001.model.dto.CreditCardReloadDTO;
import com.bank.inc.Snapshots001.model.dto.CreditCardRequestDTO;
import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import com.bank.inc.Snapshots001.model.entity.CreditCardEntity;
import com.bank.inc.Snapshots001.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/card")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("/{productId}/number")
    public ResponseEntity<ResponseDTO> createCardNumber(@PathVariable String productId)  {
        try {
            return creditCardService.generateCardNumber(productId);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }

    }

    @PostMapping("/enroll")
    public ResponseEntity<ResponseDTO> activateCreditCard(@RequestBody CreditCardRequestDTO cardRequestDTO) throws CreditCardExeptions {
        try {
            return creditCardService.activeCard(cardRequestDTO, true);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }

    }

    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<ResponseDTO> activateCreditCard(@PathVariable String cardNumber) throws CreditCardExeptions {
        try {
            CreditCardRequestDTO creditCardRequestDTO = new CreditCardRequestDTO();
            creditCardRequestDTO.setCardNumber(cardNumber);
            return creditCardService.deleteCreditCard(creditCardRequestDTO);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }
    }

    @PostMapping("/balance")
    public ResponseEntity<ResponseDTO> realodCreditCard(@RequestBody CreditCardReloadDTO creditCardReloadDTO) throws CreditCardExeptions {
        try {
            return creditCardService.reloadCard(creditCardReloadDTO);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }

    }

    @GetMapping("/balance/{cardNumber}")
    public ResponseEntity<ResponseDTO> getBalanceCard(@PathVariable String cardNumber)  {
        try {
            CreditCardRequestDTO creditCardRequestDTO = new CreditCardRequestDTO();
            creditCardRequestDTO.setCardNumber(cardNumber);
            return creditCardService.getBalanceCard(creditCardRequestDTO);
        }catch (Exception exception){
            MessageError messageError = new MessageError();
            messageError.setCode(HttpStatus.BAD_REQUEST.value());
            messageError.setMessage(exception.getMessage());
            return ResponseEntity.ok(new ResponseDTO(new CreditCardEntity(),messageError));
        }

    }

}

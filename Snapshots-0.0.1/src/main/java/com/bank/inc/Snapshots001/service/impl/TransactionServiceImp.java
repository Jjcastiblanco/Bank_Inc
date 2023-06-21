package com.bank.inc.Snapshots001.service.impl;

import com.bank.inc.Snapshots001.model.MessageError;
import com.bank.inc.Snapshots001.model.dto.PurchaseDTO;
import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import com.bank.inc.Snapshots001.model.dto.TransactionCancelDTO;
import com.bank.inc.Snapshots001.model.dto.TransactionIdDTO;
import com.bank.inc.Snapshots001.model.entity.CreditCardEntity;
import com.bank.inc.Snapshots001.model.entity.TransactionEntity;
import com.bank.inc.Snapshots001.model.enums.ErrorEnum;
import com.bank.inc.Snapshots001.model.enums.TransectionEnum;
import com.bank.inc.Snapshots001.repository.CreditCardRepository;

import com.bank.inc.Snapshots001.repository.TransactionRepository;
import com.bank.inc.Snapshots001.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TransactionServiceImp implements TransactionService {

    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImp(CreditCardRepository creditCardRepository, TransactionRepository transactionRepository) {
        this.creditCardRepository = creditCardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ResponseEntity<ResponseDTO> purchaseCreditCard(PurchaseDTO purchaseDTO) {
        CreditCardEntity creditCardEntity = creditCardRepository.findByCardNumber(purchaseDTO.getCardNumber());
        return getResponseDTOResponseEntity(purchaseDTO, creditCardEntity);

    }

    private ResponseEntity<ResponseDTO> getResponseDTOResponseEntity(PurchaseDTO purchaseDTO, CreditCardEntity creditCardEntity) {
        TransactionEntity transactionEntity = new TransactionEntity();
        if(creditCardEntity.getBalance().subtract(purchaseDTO.getAmount()).compareTo(BigDecimal.ZERO)>=0){
            creditCardEntity.setBalance(creditCardEntity.getBalance().subtract(purchaseDTO.getAmount()));
            transactionEntity.setCreditCard(creditCardEntity);
            transactionEntity.setStatus(TransectionEnum.APPROVE.getValue());
            transactionEntity.setDatePay(new Timestamp(new Date().getTime()));
            transactionEntity.setAmount(purchaseDTO.getAmount());
            TransactionEntity response = transactionRepository.save(transactionEntity);
            MessageError messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
            ResponseDTO responseDTO = new ResponseDTO(response,messageError);
            return ResponseEntity.ok(responseDTO);
        }else {
            transactionEntity.setCreditCard(creditCardEntity);
            transactionEntity.setStatus(TransectionEnum.REJECT.getValue());
            transactionEntity.setDatePay(new Timestamp(new Date().getTime()));
            TransactionEntity response = transactionRepository.save(transactionEntity);
            MessageError messageError = getMessageError(ErrorEnum.INCOMPLETE_NUMBERS.getId(), ErrorEnum.INCOMPLETE_NUMBERS.getDescription());
            ResponseDTO responseDTO = new ResponseDTO(response,messageError);
            return ResponseEntity.ok(responseDTO);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getPurchaseCreditCard(TransactionIdDTO transactionIdDTO) {
        Optional<TransactionEntity> transactionEntity = getTransactionEntity(transactionIdDTO);
        TransactionEntity transaction = new TransactionEntity();
        transactionEntity.ifPresent(result -> {
            transaction.setIdTransaction(result.getIdTransaction());
            transaction.setCreditCard(result.getCreditCard());
            transaction.setStatus(result.getStatus());
            transaction.setAmount(result.getAmount());
        });
        MessageError messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
        ResponseDTO responseDTO = new ResponseDTO(transaction,messageError);
        return ResponseEntity.ok(responseDTO);
    }

    private Optional<TransactionEntity> getTransactionEntity(TransactionIdDTO transactionIdDTO) {
        return transactionRepository.findById(Long.parseLong(transactionIdDTO.getTransactionId()));
    }

    @Override
    public ResponseEntity<ResponseDTO> cancelPurchaseCreditCard(TransactionCancelDTO transactionCancelDTO) {
        Optional<TransactionEntity> transactionEntity =  transactionRepository.findById(Long.parseLong(transactionCancelDTO.getTransactionId()));
        AtomicReference<TransactionEntity> transaction = new AtomicReference<>(new TransactionEntity());
        AtomicReference<CreditCardEntity> creditCard = new AtomicReference<>(new CreditCardEntity());
        transactionEntity.ifPresent(p -> {
            LocalDateTime  transactionDate = p.getDatePay().toLocalDateTime();
            LocalDateTime actualdate = LocalDateTime.now();
            long hourDiferences= ChronoUnit.HOURS.between(transactionDate, actualdate);
            if(hourDiferences <= 24){
                transaction.get().setIdTransaction(p.getIdTransaction());
                creditCard.set(p.getCreditCard());
                creditCard.get().setBalance(creditCard.get().getBalance().add(p.getAmount()));
                transaction.get().setCreditCard(creditCard.get());
                transaction.get().setCreditCard(p.getCreditCard());
                transaction.get().setStatus(TransectionEnum.CANCEL.getValue());
                transaction.get().setAmount(p.getAmount());
                transaction.get().setDatePay(p.getDatePay());
            }
        });
        TransactionEntity response = transactionRepository.save(transaction.get());
        MessageError messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
        ResponseDTO responseDTO = new ResponseDTO(response,messageError);
        return ResponseEntity.ok(responseDTO);
    }

    private MessageError getMessageError(int idError, String descriptionError) {
        MessageError messageError = new MessageError();
        messageError.setCode(idError);
        messageError.setMessage(descriptionError);
        return messageError;
    }
}

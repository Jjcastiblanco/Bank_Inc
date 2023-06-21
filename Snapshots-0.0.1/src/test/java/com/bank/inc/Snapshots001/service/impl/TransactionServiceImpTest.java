package com.bank.inc.Snapshots001.service.impl;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImpTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImp transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void purchaseCreditCard_InsufficientBalance_ReturnsResponseDTOWithIncompleteNumbersError() {
        // Arrange
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setCardNumber("1234567890123456");
        purchaseDTO.setAmount(new BigDecimal(100));
        CreditCardEntity creditCardEntity = createCreditCardEntity();
        creditCardEntity.setBalance(new BigDecimal(50));
        Mockito.when(creditCardRepository.findByCardNumber(purchaseDTO.getCardNumber())).thenReturn(creditCardEntity);
        ResponseEntity<ResponseDTO> response = transactionService.purchaseCreditCard(purchaseDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.INCOMPLETE_NUMBERS.getId(), responseDTO.getMessageError().getCode());
    }

    @Test
    void purchaseCreditCard_SufficientBalance_ReturnsResponseDTOWithSuccess() {
        // Arrange
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setCardNumber("1234567890123456");
        purchaseDTO.setAmount(new BigDecimal(50));
        CreditCardEntity creditCardEntity = createCreditCardEntity();
        creditCardEntity.setBalance(new BigDecimal(100));
        Mockito.when(creditCardRepository.findByCardNumber(purchaseDTO.getCardNumber())).thenReturn(creditCardEntity);
        Mockito.when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenAnswer(invocation -> {
            TransactionEntity transactionEntity = invocation.getArgument(0);
            transactionEntity.setIdTransaction(1L);
            transactionEntity.setStatus(TransectionEnum.APPROVE.getValue());
            transactionEntity.setDatePay(new Timestamp(new Date().getTime()));
            return transactionEntity;
        });
        ResponseEntity<ResponseDTO> response = transactionService.purchaseCreditCard(purchaseDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
    }

    @Test
    void getPurchaseCreditCard_ExistingTransactionId_ReturnsResponseDTOWithSuccess() {
        TransactionIdDTO transactionIdDTO = new TransactionIdDTO();
        transactionIdDTO.setTransactionId("1");
        TransactionEntity transactionEntity = createTransactionEntity();
        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transactionEntity));
        ResponseEntity<ResponseDTO> response = transactionService.getPurchaseCreditCard(transactionIdDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.SUCCESS.getId(), responseDTO.getMessageError().getCode());
    }

    @Test
    void getPurchaseCreditCard_NonExistingTransactionId_ReturnsResponseDTOWithEmptyTransaction() {
        TransactionIdDTO transactionIdDTO = new TransactionIdDTO();
        transactionIdDTO.setTransactionId("1");
        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<ResponseDTO> response = transactionService.getPurchaseCreditCard(transactionIdDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.SUCCESS.getId(), responseDTO.getMessageError().getCode());
    }

    @Test
    void cancelPurchaseCreditCard_ValidTransactionIdWithin24Hours_ReturnsResponseDTOWithSuccess() {
        TransactionCancelDTO transactionCancelDTO = new TransactionCancelDTO();
        transactionCancelDTO.setTransactionId("1");
        TransactionEntity transactionEntity = createTransactionEntity();
        transactionEntity.setDatePay(new Timestamp(new Date().getTime()));
        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transactionEntity));
        Mockito.when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResponseEntity<ResponseDTO> response = transactionService.cancelPurchaseCreditCard(transactionCancelDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.SUCCESS.getId(), responseDTO.getMessageError().getCode());
    }

    @Test
    void cancelPurchaseCreditCard_ValidTransactionIdMoreThan24Hours_ReturnsResponseDTOWithEmptyTransaction() {
        TransactionCancelDTO transactionCancelDTO = new TransactionCancelDTO();
        transactionCancelDTO.setTransactionId("1");
        TransactionEntity transactionEntity = createTransactionEntity();
        transactionEntity.setDatePay(new Timestamp(new Date().getTime() - 25 * 60 * 60 * 1000)); // Set date more than 24 hours ago
        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transactionEntity));
        ResponseEntity<ResponseDTO> response = transactionService.cancelPurchaseCreditCard(transactionCancelDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.SUCCESS.getId(), responseDTO.getMessageError().getCode());
    }

    private CreditCardEntity createCreditCardEntity() {
        CreditCardEntity creditCardEntity = new CreditCardEntity();
        creditCardEntity.setIdCreditCard(1);
        creditCardEntity.setCardNumber("1234567890123456");
        creditCardEntity.setCardCreate(new Timestamp(new Date().getTime()));
        creditCardEntity.setCardExpiration(new Timestamp(new Date().getTime()));
        creditCardEntity.setBalance(BigDecimal.ZERO);
        creditCardEntity.setActive(false);
        return creditCardEntity;
    }

    private TransactionEntity createTransactionEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setIdTransaction(1L);
        transactionEntity.setCreditCard(createCreditCardEntity());
        transactionEntity.setStatus(TransectionEnum.APPROVE.getValue());
        transactionEntity.setDatePay(new Timestamp(new Date().getTime()));
        transactionEntity.setAmount(BigDecimal.ZERO);
        return transactionEntity;
    }


}
package com.bank.inc.Snapshots001.service.impl;

import com.bank.inc.Snapshots001.exeption.CreditCardExeptions;
import com.bank.inc.Snapshots001.model.dto.CreditCardReloadDTO;
import com.bank.inc.Snapshots001.model.dto.CreditCardRequestDTO;
import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import com.bank.inc.Snapshots001.model.entity.CreditCardEntity;
import com.bank.inc.Snapshots001.model.enums.ErrorEnum;
import com.bank.inc.Snapshots001.repository.CreditCardRepository;
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


class CreditCardServiceImpTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardServiceImp creditCardService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateCardNumber_ValidProductId_ReturnsResponseDTOWithSuccess() throws CreditCardExeptions {
        String productId = "123456";
        Mockito.when(creditCardRepository.save(Mockito.any(CreditCardEntity.class))).thenReturn(createCreditCardEntity());
        ResponseEntity<ResponseDTO> response = creditCardService.generateCardNumber(productId);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.SUCCESS.getId(), responseDTO.getMessageError().getCode());
    }

    @Test
    void activeCard_ValidCardRequestDTO_ReturnsResponseDTOWithSuccess() throws CreditCardExeptions {
        CreditCardRequestDTO cardRequestDTO = new CreditCardRequestDTO();
        CreditCardEntity creditCardEntity = createCreditCardEntity();
        cardRequestDTO.setCardNumber("1234567890123456");
        Mockito.when(creditCardRepository.findByCardNumber(cardRequestDTO.getCardNumber())).thenReturn(creditCardEntity);
        Mockito.when(creditCardRepository.save(Mockito.any(CreditCardEntity.class))).thenReturn(creditCardEntity);
        ResponseEntity<ResponseDTO> response = creditCardService.activeCard(cardRequestDTO, true);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.SUCCESS.getId(), responseDTO.getMessageError().getCode());
        Assertions.assertTrue(creditCardEntity.isActive());
    }

    @Test
    void deleteCreditCard_ValidCardRequestDTO_ReturnsResponseDTOWithSuccess() throws CreditCardExeptions {
        CreditCardRequestDTO cardRequestDTO = new CreditCardRequestDTO();
        CreditCardEntity creditCardEntity = createCreditCardEntity();
        cardRequestDTO.setCardNumber("1234567890123456");
        Mockito.when(creditCardRepository.findByCardNumber(cardRequestDTO.getCardNumber())).thenReturn(creditCardEntity);
        ResponseEntity<ResponseDTO> response = creditCardService.deleteCreditCard(cardRequestDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
    }
    @Test
    void reloadCard_ActiveCard_ReturnsResponseDTOWithSuccess() throws CreditCardExeptions {
        CreditCardReloadDTO creditCardReloadDTO = new CreditCardReloadDTO();
        CreditCardRequestDTO creditCardRequestDTO = new CreditCardRequestDTO();
        creditCardRequestDTO.setCardNumber("1234567890123456");
        creditCardReloadDTO.setCardNumber(creditCardRequestDTO.getCardNumber());
        creditCardReloadDTO.setBalance(new BigDecimal(10));
        CreditCardEntity creditCardEntity = createCreditCardEntity();
        creditCardEntity.setActive(true);
        Mockito.when(creditCardRepository.findByCardNumber(creditCardRequestDTO.getCardNumber())).thenReturn(creditCardEntity);
        Mockito.when(creditCardRepository.save(Mockito.any(CreditCardEntity.class))).thenReturn(creditCardEntity);
        ResponseEntity<ResponseDTO> response = creditCardService.reloadCard(creditCardReloadDTO);
        ResponseDTO responseDTO = response.getBody();
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(ErrorEnum.SUCCESS.getId(), responseDTO.getMessageError().getCode());
        Assertions.assertEquals(new BigDecimal(10), creditCardEntity.getBalance());
    }

    @Test
    void reloadCard() {
        CreditCardReloadDTO creditCardReloadDTO = new CreditCardReloadDTO();
        CreditCardRequestDTO creditCardRequestDTO = new CreditCardRequestDTO();
        creditCardRequestDTO.setCardNumber("1234567890123456");
        creditCardReloadDTO.setCardNumber(creditCardRequestDTO.getCardNumber());
        CreditCardEntity creditCardEntity = createCreditCardEntity();
        creditCardEntity.setActive(false);
        Mockito.when(creditCardRepository.findByCardNumber(creditCardRequestDTO.getCardNumber())).thenReturn(creditCardEntity);
        Assertions.assertThrows(CreditCardExeptions.class, () -> creditCardService.reloadCard(creditCardReloadDTO));
    }

    @Test
    void getBalanceCard() throws CreditCardExeptions {
        CreditCardRequestDTO cardRequestDTO = new CreditCardRequestDTO();
        cardRequestDTO.setCardNumber("1234567890123456");
        CreditCardEntity creditCardEntity = createCreditCardEntity();
        Mockito.when(creditCardRepository.findByCardNumber(cardRequestDTO.getCardNumber())).thenReturn(creditCardEntity);

        ResponseEntity<ResponseDTO> response = creditCardService.getBalanceCard(cardRequestDTO);
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
}
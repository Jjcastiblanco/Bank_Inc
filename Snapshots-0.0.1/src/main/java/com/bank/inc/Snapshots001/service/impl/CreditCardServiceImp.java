package com.bank.inc.Snapshots001.service.impl;

import com.bank.inc.Snapshots001.exeption.CreditCardExeptions;
import com.bank.inc.Snapshots001.model.MessageError;
import com.bank.inc.Snapshots001.model.dto.CreditCardReloadDTO;
import com.bank.inc.Snapshots001.model.dto.CreditCardRequestDTO;
import com.bank.inc.Snapshots001.model.dto.ResponseDTO;
import com.bank.inc.Snapshots001.model.entity.CreditCardEntity;
import com.bank.inc.Snapshots001.model.enums.Constant;
import com.bank.inc.Snapshots001.model.enums.ErrorEnum;
import com.bank.inc.Snapshots001.repository.CreditCardRepository;
import com.bank.inc.Snapshots001.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CreditCardServiceImp implements CreditCardService {


    private final CreditCardRepository creditCardRepository;

    @Autowired
    public CreditCardServiceImp(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }


    @Override
    public ResponseEntity<ResponseDTO> generateCardNumber(String productId) throws CreditCardExeptions {
        validateLongNumberProductId(productId);
        CreditCardEntity creditCardEntity = new CreditCardEntity();
        creditCardEntity.setCardCreate(new Timestamp(new Date().getTime()));
        creditCardEntity.setCardNumber(getNumberCardComplete(productId));
        creditCardEntity.setCardExpiration(getExpirationCard(creditCardEntity.getCardCreate()));
        creditCardEntity.setBalance(new BigDecimal(0));
        creditCardEntity.setActive(false);
        MessageError messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
        ResponseDTO responseDTO = getResponseDTO(creditCardEntity, messageError);

        return ResponseEntity.ok(responseDTO);}

    @Override
    public ResponseEntity<ResponseDTO> activeCard(CreditCardRequestDTO cardRequestDTO, boolean activeCard) throws CreditCardExeptions {
        CreditCardEntity creditCardEntity = getCreditCardEntity(cardRequestDTO);
        creditCardEntity.setActive(activeCard);
        MessageError messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
        ResponseDTO responseDTO = getResponseDTO(creditCardEntity,messageError);
        return ResponseEntity.ok(responseDTO);
    }



    @Override
    public ResponseEntity<ResponseDTO> deleteCreditCard(CreditCardRequestDTO cardRequestDTO) throws CreditCardExeptions {
        CreditCardEntity creditCardEntity = getCreditCardEntity(cardRequestDTO);
        ResponseDTO responseDTO;
        MessageError messageError = getMessageError(creditCardEntity);
        responseDTO = new ResponseDTO(creditCardEntity,messageError);
        return ResponseEntity.ok(responseDTO);
    }


    @Override
    public ResponseEntity<ResponseDTO> reloadCard(CreditCardReloadDTO creditCardReloadDTO) throws CreditCardExeptions {
        CreditCardRequestDTO creditCardRequestDTO = new CreditCardRequestDTO();
        creditCardRequestDTO.setCardNumber(creditCardReloadDTO.getCardNumber());
        CreditCardEntity creditCardEntity = getCreditCardEntity(creditCardRequestDTO);
        MessageError messageError;
        ResponseDTO responseDTO;
        if(creditCardEntity.isActive()){
            creditCardEntity.setBalance(creditCardEntity.getBalance().add(creditCardReloadDTO.getBalance()));
            messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
            responseDTO = getResponseDTO(creditCardEntity,messageError);
        }else {
            messageError = getMessageError(ErrorEnum.INCOMPLETE_NUMBERS.getId(), ErrorEnum.SUCCESS.getDescription());
            responseDTO = getResponseDTO(creditCardEntity,messageError);
            throw new CreditCardExeptions(responseDTO);
        }
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<ResponseDTO> getBalanceCard(CreditCardRequestDTO cardRequestDTO) throws CreditCardExeptions {
        CreditCardEntity creditCardEntity = getCreditCardEntity(cardRequestDTO);
        MessageError messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
        ResponseDTO responseDTO = new ResponseDTO(creditCardEntity,messageError);
        return ResponseEntity.ok(responseDTO);
    }
    @Override
    public void pay() {

    }

    private String getNumberCardComplete(String productId){
        int otherNumber = new Random().nextInt(1000000000);
        return String.format(Constant.FORMAT_CREDIT_CARD.getValue(), Integer.valueOf(productId), otherNumber);

    }

    private Timestamp getExpirationCard(Timestamp cardCreate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cardCreate.getTime());
        calendar.add(Calendar.YEAR, 3);
        return new Timestamp(calendar.getTimeInMillis());
    }

    private void validateLongNumberProductId(String productId) throws CreditCardExeptions {
        if( productId.length() != 6 && containsLetter(productId)  ){
            MessageError messageError = new MessageError();
            messageError.setCode(ErrorEnum.INCOMPLETE_NUMBERS.getId());
            messageError.setMessage(ErrorEnum.INCOMPLETE_NUMBERS.getDescription());
            CreditCardEntity creditCardEntity = new CreditCardEntity();
            ResponseDTO responseDTO = new ResponseDTO(creditCardEntity, messageError);
            throw new CreditCardExeptions(responseDTO);
        }
    }
    private boolean containsLetter(String input) {
        Pattern pattern = Pattern.compile(Constant.REGEX_VALIDATE_CREDIT_CARD.getValue());
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    private ResponseDTO getResponseDTO(CreditCardEntity creditCardEntity, MessageError messageError) {
        CreditCardEntity responseEntity = creditCardRepository.save(creditCardEntity);
        return new ResponseDTO(responseEntity, messageError);
    }

    private MessageError getMessageError(int idError, String descriptionError) {
        MessageError messageError = new MessageError();
        messageError.setCode(idError);
        messageError.setMessage(descriptionError);
        return messageError;
    }

    private void validateCardNumberLong(CreditCardRequestDTO cardRequestDTO, CreditCardEntity creditCardEntity) throws CreditCardExeptions {
        MessageError messageError;
        if(isCreditCardValid(cardRequestDTO)){
            messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
            creditCardEntity.setCardNumber(cardRequestDTO.getCardNumber());
            ResponseDTO responseDTO = new ResponseDTO(creditCardEntity,messageError);
            throw new CreditCardExeptions(responseDTO);
        }
    }

    private boolean isCreditCardValid(CreditCardRequestDTO cardRequestDTO) {
        return containsLetter(cardRequestDTO.getCardNumber()) && cardRequestDTO.getCardNumber().length() != 16;
    }

    private CreditCardEntity getCreditCardEntity(CreditCardRequestDTO cardRequestDTO) throws CreditCardExeptions {
        CreditCardEntity creditCardEntity = new CreditCardEntity();
        validateCardNumberLong(cardRequestDTO, creditCardEntity);
        creditCardEntity = creditCardRepository.findByCardNumber(cardRequestDTO.getCardNumber());
        return creditCardEntity;
    }

    private MessageError getMessageError(CreditCardEntity creditCardEntity) {
        MessageError messageError;
        if(creditCardEntity.isActive()){
            messageError = getMessageError(ErrorEnum.SUCCESS.getId(), ErrorEnum.SUCCESS.getDescription());
            creditCardRepository.deleteById(creditCardEntity.getIdCreditCard());
        }else {
            messageError = getMessageError(ErrorEnum.INCOMPLETE_NUMBERS.getId(), ErrorEnum.INCOMPLETE_NUMBERS.getDescription());
        }
        return messageError;
    }
}

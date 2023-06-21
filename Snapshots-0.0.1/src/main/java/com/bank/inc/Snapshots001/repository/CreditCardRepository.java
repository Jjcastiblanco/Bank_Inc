package com.bank.inc.Snapshots001.repository;

import com.bank.inc.Snapshots001.model.entity.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Integer> {

    CreditCardEntity findByCardNumber(String cardNumber);

}

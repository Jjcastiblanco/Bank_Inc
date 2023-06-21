package com.bank.inc.Snapshots001.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "credicard")
public class CreditCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credit_card")
    private Integer idCreditCard;
    @Column(name = "card_number", unique = true)
    private String cardNumber;
    @Column(name = "card_create")
    private Timestamp cardCreate;
    @Column(name = "card_expiration")
    private Timestamp cardExpiration;
    @Column(name = "card_active")
    private boolean isActive;
    private BigDecimal balance;

}

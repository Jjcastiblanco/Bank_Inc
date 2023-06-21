package com.bank.inc.Snapshots001.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction")
    private Long idTransaction;

    @ManyToOne
    private CreditCardEntity creditCard;

    @Column(name = "date_pay")
    private Timestamp datePay;

    private String status;

    private BigDecimal amount;

}

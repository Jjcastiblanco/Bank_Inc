package com.bank.inc.Snapshots001.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "costumer")
public class CustomerEntity {

    @Id
    @Column(unique = true)
    private Integer id;
    private String name;
    private String lastName;
}

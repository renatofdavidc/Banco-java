package com.example.BancoRenatoGustavoJava.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long cpf;
    private Long agencyNumber;
    private String name;
    private String openingDate;
    private String accountType;
    private Double balance;
    private boolean active;

    public Account(Long cpf, Long agencyNumber, String name, String openingDate, String accountType, Double balance,
            boolean active) {
        this.cpf = cpf;
        this.agencyNumber = agencyNumber;
        this.name = name;
        this.openingDate = openingDate;
        this.accountType = accountType;
        this.balance = balance;
        this.active = active;
    }

    public Long getCpf() {
        return cpf;
    }
    public Long getAgencyNumber() {
        return agencyNumber;
    }
    public String getName() {
        return name;
    }
    public String getOpeningDate() {
        return openingDate;
    }
    public String getAccountType() {
        return accountType;
    }
    public Double getBalance() {
        return balance;
    }
    public boolean isActive() {
        return active;
    }
    
}

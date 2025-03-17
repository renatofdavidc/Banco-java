package com.example.BancoRenatoGustavoJava.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long cpf;

    @Column(nullable = false)
    private Long agencyNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String openingDate;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private Double balance;

    private boolean active;

    
    public Account() {}

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

    
    public Long getId() {
        return id;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Long getAgencyNumber() {
        return agencyNumber;
    }

    public void setAgencyNumber(Long agencyNumber) {
        this.agencyNumber = agencyNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

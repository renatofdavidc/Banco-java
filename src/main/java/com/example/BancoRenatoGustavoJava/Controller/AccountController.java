package com.example.BancoRenatoGustavoJava.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BancoRenatoGustavoJava.Model.Account;
import com.example.BancoRenatoGustavoJava.Repository.AccountRepository;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public Map<String, String> getInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("projeto", "Projeto BANCO RENATO GUSTAVO");
        response.put("alunos", "Renato de Freitas David Campiteli e Gustavo Lopes Santos da Silva");
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Account account) {
        if (account.getName() == null || account.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do titular é obrigatório.");
        }

        // Validação do CPF do titular
        if (account.getCpf() == null || account.getCpf() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF do titular é obrigatório e deve ser válido.");
        }

        try {
            LocalDate openingDate = LocalDate.parse(account.getOpeningDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (openingDate.isAfter(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de abertura da conta não pode ser no futuro.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de data inválido. Use 'yyyy-MM-dd'.");
        }

        if (account.getBalance() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O saldo inicial não pode ser negativo.");
        }

        if (!"corrente".equalsIgnoreCase(account.getAccountType()) &&
            !"poupanca".equalsIgnoreCase(account.getAccountType()) &&
            !"salario".equalsIgnoreCase(account.getAccountType())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de conta inválido. Use 'corrente', 'poupanca' ou 'salario'.");
        }

        System.out.println("Cadastrando o cliente " + account.getName());
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.status(201).body(savedAccount);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Account>> listAll() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

}

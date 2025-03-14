package com.example.BancoRenatoGustavoJava.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.BancoRenatoGustavoJava.Model.Account;

@RestController
public class AccountController {

    private List<Account> clients = new ArrayList<>();
    
    @GetMapping("/")
    public Map<String, String> getInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("projeto", "Projeto BANCO RENATO GUSTAVO");
        response.put("desenvolvedores", "Desenvolvido por Renato de Freitas David Campiteli e Gustavo Lopes Santos da Silva");
        
        return response;
    }

    @PostMapping("/createAccount")
    public ResponseEntity<Account> create (@RequestBody Account account){
        System.out.println("Cadastrando o cliente "+ account.getName());
        clients.add(account);
        return ResponseEntity.status(201).body(account);
    }
}

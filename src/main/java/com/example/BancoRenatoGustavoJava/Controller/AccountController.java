package com.example.BancoRenatoGustavoJava.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.BancoRenatoGustavoJava.Model.Account;
import com.example.BancoRenatoGustavoJava.Repository.AccountRepository;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public Map<String, String> getInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("projeto", "Projeto DS Bank");
        response.put("alunos", "Renato de Freitas David Campiteli e Gustavo Lopes Santos da Silva");
        return response;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Account account) {
        if (account.getName() == null || account.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do titular é obrigatório.");
        }

        
        if (account.getCpf() == null || account.getCpf() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CPF do titular é obrigatório e deve ser válido.");
        }

        try {
            LocalDate openingDate = LocalDate.parse(account.getOpeningDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (openingDate.isAfter(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("A data de abertura da conta não pode ser no futuro.");
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tipo de conta inválido. Use 'corrente', 'poupanca' ou 'salario'.");
        }

        System.out.println("Cadastrando o cliente " + account.getName());
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.status(201).body(savedAccount);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Account>> listAll() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("{id}")
    public Account get(@PathVariable Long id) {
        log.info("Buscando conta com ID: " + id);
        return getAccount(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Account getByCpf(@PathVariable Long cpf) {
        log.info("Buscando conta com CPF: " + cpf);
        return getAccountByCpf(cpf);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        log.info("Apagando conta bancária.." + id);
        accountRepository.delete(getAccount(id));
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<?> closeAccount(@PathVariable Long id) {
        log.info("Encerrando conta com ID: " + id);
        Account account = getAccount(id);
        if (!account.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A conta já está encerrada.");
        }

        account.setActive(false);
        accountRepository.save(account);

        return ResponseEntity.ok("Conta com ID " + id + " foi encerrada com sucesso.");
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Map<String, Object> request) {
        log.info("Iniciando depósito na conta");

        
        Long id = ((Number) request.get("id")).longValue();
        Double depositAmount = ((Number) request.get("amount")).doubleValue();

        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID da conta é obrigatório e deve ser válido.");
        }

        if (depositAmount == null || depositAmount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O valor do depósito deve ser maior que zero.");
        }

        Account account = getAccount(id);

        if (!account.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A conta está encerrada e não pode receber depósitos.");
        }

        
        account.setBalance(account.getBalance() + depositAmount);
        accountRepository.save(account);

        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Depósito de R$ " + depositAmount + " realizado com sucesso.");
        response.put("account", account);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, Object> request) {
        log.info("Iniciando saque na conta");
        
        Long id = ((Number) request.get("id")).longValue();
        Double withdrawAmount = ((Number) request.get("amount")).doubleValue();
        log.info("Valor do saque: " + withdrawAmount);
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID da conta é obrigatório e deve ser válido.");
        }

        if (withdrawAmount == null || withdrawAmount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O valor do saque deve ser maior que zero.");
        }

        Account account = getAccount(id);
        if (!account.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A conta está encerrada e não pode realizar saques.");
        }
        if (withdrawAmount > account.getBalance()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente para realizar o saque.");
        }
        
        account.setBalance(account.getBalance() - withdrawAmount);
        accountRepository.save(account);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Saque de R$ " + withdrawAmount + " realizado com sucesso.");
        response.put("account", account);     

        return ResponseEntity.ok(response);
    }

    @PutMapping("/pix")
    public ResponseEntity<?> transfer(@RequestBody Map<String, Object> request) {
        log.info("Iniciando transferência pix");

        Long originAccountId = ((Number) request.get("originAccountId")).longValue();
        Long destinationAccountId = ((Number) request.get("destinationAccountId")).longValue();
        Double transferAmount = ((Number) request.get("amount")).doubleValue();
        log.info("Valor do pix: " + transferAmount);
        if (originAccountId == null || originAccountId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID da conta de origem é obrigatório e deve ser válido."); 
        }
        if (destinationAccountId == null || destinationAccountId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID da conta de destino é obrigatório e deve ser válido.");
        }
        if (transferAmount == null || transferAmount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O valor do pix deve ser maior que zero.");
        }

        Account originAccount = getAccount(originAccountId);
        Account destinationAccount = getAccount(destinationAccountId);

        if (!originAccount.isActive() || !destinationAccount.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A conta de origem ou de destino está encerrada e não pode fazer ou receber pix.");
        }

        if (transferAmount > originAccount.getBalance()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente para realizar o pix.");
        }

        originAccount.setBalance(originAccount.getBalance() - transferAmount);
        destinationAccount.setBalance(destinationAccount.getBalance() + transferAmount);
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pix de R$ " + transferAmount + " realizada com sucesso.");
        response.put("originAccount", originAccount);
        response.put("destinationAccount", destinationAccount);

        return ResponseEntity.ok(response);
    }

    private Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    private Account getAccountByCpf(Long cpf) {
        return accountRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Conta não encontrada para o CPF informado"));
    }

}

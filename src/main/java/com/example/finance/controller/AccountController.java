package com.example.finance.controller;

import com.example.finance.model.Account;
import com.example.finance.model.Transaction;
import com.example.finance.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "message", "Finance Tracker API is running",
                "endpoints", "/api/accounts, /api/accounts/{id}/deposit, /api/accounts/{id}/withdraw"
        );
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> body) {
        String ownerName = (String) body.get("ownerName");
        BigDecimal opening = body.get("openingBalance") != null
                ? new BigDecimal(body.get("openingBalance").toString())
                : BigDecimal.ZERO;
        Account created = accountService.createAccount(ownerName, opening);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/accounts")
    public List<Account> listAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<Transaction> deposit(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        String description = (String) body.getOrDefault("description", "Deposit");
        Transaction tx = accountService.deposit(id, amount, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(tx);
    }

    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<Transaction> withdraw(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        String description = (String) body.getOrDefault("description", "Withdrawal");
        Transaction tx = accountService.withdraw(id, amount, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(tx);
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<Transaction> transactions(@PathVariable Long id) {
        return accountService.getTransactionsForAccount(id);
    }
}

package com.restapp.bank.controllers;

import com.restapp.bank.models.Account;
import com.restapp.bank.models.CreateAccountRequest;
import com.restapp.bank.models.MoneyRequest;
import com.restapp.bank.models.Transaction;
import com.restapp.bank.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @GetMapping("/{id}")
    public Account getAccountDetails(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/{id}/deposit")
    public Account depositMoney(@PathVariable Long id, @RequestBody MoneyRequest request) {
        return accountService.deposit(id, request);
    }

    @PostMapping("/{id}/withdraw")
    public Account withdrawMoney(@PathVariable Long id, @RequestBody MoneyRequest request) {
        return accountService.withdraw(id, request);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> transactionHistory(@PathVariable Long id) {
        return accountService.getTransactions(id);
    }
}
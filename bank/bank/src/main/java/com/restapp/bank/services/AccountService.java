package com.restapp.bank.services;

import com.restapp.bank.models.Account;
import com.restapp.bank.models.CreateAccountRequest;
import com.restapp.bank.models.MoneyRequest;
import com.restapp.bank.models.Transaction;
import com.restapp.bank.models.User;
import com.restapp.bank.repos.AccountRepository;
import com.restapp.bank.repos.TransactionRepository;
import com.restapp.bank.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(CreateAccountRequest request) {
        User user = userRepository.findById(request.userId());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        long nextId = accountRepository.findAll().stream()
                .mapToLong(account -> account.getId())
                .max()
                .orElse(0L) + 1L;

        String accountNumber = "ACC-" + (1000 + nextId);
        Account account = new Account(nextId, accountNumber, request.accountType(), 0.0, request.userId());
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    public Account deposit(Long accountId, MoneyRequest request) {
        validateAmount(request.amount());
        Account account = getAccount(accountId);
        account.setBalance(account.getBalance() + request.amount());
        recordTransaction(accountId, "DEPOSIT", request.amount(), "Deposit made");
        return account;
    }

    public Account withdraw(Long accountId, MoneyRequest request) {
        validateAmount(request.amount());
        Account account = getAccount(accountId);
        if (account.getBalance() < request.amount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - request.amount());
        recordTransaction(accountId, "WITHDRAWAL", request.amount(), "Withdrawal made");
        return account;
    }

    public List<Transaction> getTransactions(Long accountId) {
        getAccount(accountId);
        return transactionRepository.findByAccountId(accountId);
    }

    private void recordTransaction(Long accountId, String type, Double amount, String description) {
        long nextId = transactionRepository.findAll().stream()
                .mapToLong(transaction -> transaction.getId())
                .max()
                .orElse(0L) + 1L;
        transactionRepository.save(new Transaction(nextId, accountId, type, amount, description));
    }

    private void validateAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
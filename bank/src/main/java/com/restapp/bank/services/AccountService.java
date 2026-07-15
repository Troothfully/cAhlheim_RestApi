package com.restapp.bank.services;

import com.restapp.bank.models.Account;
import com.restapp.bank.models.CreateAccountRequest;
import com.restapp.bank.models.CreateAccountResponse;
import com.restapp.bank.models.MoneyRequest;
import com.restapp.bank.models.Transaction;
import com.restapp.bank.models.TransactionResponse;
import com.restapp.bank.models.User;
import com.restapp.bank.repos.AccountRepository;
import com.restapp.bank.repos.TransactionRepository;
import com.restapp.bank.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        validateAccountRequest(request);
        User user = userRepository.findByEmail(request.email())
                .orElseGet(() -> createUser(request.name(), request.email()));

        int nextId = accountRepository.findAll().stream()
            .mapToInt(Account::getId)
                .max()
            .orElse(0) + 1;

        String accountNumber = "ACC-" + (1000 + nextId);
        Account account = new Account(nextId, accountNumber, request.accountType(), 0.0, user.getId());
        Account savedAccount = accountRepository.save(account);
        return new CreateAccountResponse(
                String.valueOf(savedAccount.getId()),
                user.getName(),
                user.getEmail(),
                savedAccount.getAccountType(),
                savedAccount.getBalance());
    }

    private User createUser(String name, String email) {
        int nextId = userRepository.findAll().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
        return userRepository.save(new User(nextId, name, email, "Customer"));
    }

    private void validateAccountRequest(CreateAccountRequest request) {
        if (request.name() == null || request.name().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.accountType() == null || request.accountType().isBlank()) {
            throw new IllegalArgumentException("Name, email, and account type are required");
        }
    }

        public Account getAccount(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

        public CreateAccountResponse getAccountDetails(Integer id) {
        Account account = getAccount(id);
        User user = userRepository.findById(account.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new CreateAccountResponse(
            String.valueOf(account.getId()),
            user.getName(),
            user.getEmail(),
            formatDisplayValue(account.getAccountType()),
            account.getBalance());
        }

    public Account deposit(Integer accountId, MoneyRequest request) {
        validateAmount(request.amount());
        Account account = getAccount(accountId);
        account.setBalance(account.getBalance() + request.amount());
        accountRepository.save(account);
        recordTransaction(accountId, "DEPOSIT", request.amount(), "Deposit made");
        return account;
    }

    public Account withdraw(Integer accountId, MoneyRequest request) {
        validateAmount(request.amount());
        Account account = getAccount(accountId);
        if (account.getBalance() < request.amount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - request.amount());
        accountRepository.save(account);
        recordTransaction(accountId, "WITHDRAWAL", request.amount(), "Withdrawal made");
        return account;
    }

    public List<TransactionResponse> getTransactions(Integer accountId) {
        getAccount(accountId);
        return transactionRepository.findByAccountId(accountId).stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    private void recordTransaction(Integer accountId, String type, Double amount, String description) {
        int nextId = transactionRepository.findAll().stream()
                .mapToInt(Transaction::getId)
                .max()
                .orElse(0) + 1;
        transactionRepository.save(new Transaction(nextId, accountId, type, amount, LocalDate.now(), description));
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                String.valueOf(transaction.getId()),
                formatTransactionType(transaction.getTransactionType()),
                transaction.getAmount(),
                transaction.getDate() == null ? null : transaction.getDate().toString());
    }

    private String formatTransactionType(String transactionType) {
        return formatDisplayValue(transactionType);
    }

    private String formatDisplayValue(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        String normalized = value.toLowerCase();
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }

    private void validateAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}

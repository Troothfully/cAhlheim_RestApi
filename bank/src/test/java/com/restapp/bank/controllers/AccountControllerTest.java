package com.restapp.bank.controllers;

import com.restapp.bank.repos.AccountRepository;
import com.restapp.bank.repos.TransactionRepository;
import com.restapp.bank.repos.UserRepository;
import com.restapp.bank.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.restapp.bank.models.Account;
import com.restapp.bank.models.Transaction;
import com.restapp.bank.models.User;
import java.time.LocalDate;

class AccountControllerTest {

    @Test
    void accountFlowSupportsCreateDepositWithdrawAndHistory() throws Exception {
                AccountRepository accountRepository = mock(AccountRepository.class);
                TransactionRepository transactionRepository = mock(TransactionRepository.class);
                UserRepository userRepository = mock(UserRepository.class);

                List<Account> accounts = new ArrayList<>();
                List<Transaction> transactions = new ArrayList<>();
                List<User> users = new ArrayList<>();

                when(userRepository.findByEmail("alice@example.com"))
                                .thenReturn(java.util.Optional.empty());
                when(userRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(users));
                when(userRepository.findById(anyInt())).thenAnswer(invocation ->
                                users.stream().filter(u -> u.getId().equals(invocation.getArgument(0))).findFirst());
                when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                        User user = invocation.getArgument(0);
                        users.removeIf(existing -> existing.getId().equals(user.getId()));
                        users.add(user);
                        return user;
                });

                when(accountRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(accounts));
                when(accountRepository.findById(anyInt())).thenAnswer(invocation ->
                                accounts.stream().filter(a -> a.getId().equals(invocation.getArgument(0))).findFirst());
                when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
                        Account account = invocation.getArgument(0);
                        accounts.removeIf(existing -> existing.getId().equals(account.getId()));
                        accounts.add(account);
                        return account;
                });

                when(transactionRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(transactions));
                when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
                        Transaction transaction = invocation.getArgument(0);
                        transactions.add(transaction);
                        return transaction;
                });
                when(transactionRepository.findByAccountId(anyInt())).thenAnswer(invocation ->
                                transactions.stream()
                                                .filter(t -> t.getAccountId().equals(invocation.getArgument(0)))
                                                .toList());

        AccountService accountService = new AccountService(accountRepository, transactionRepository, userRepository);
        AccountController accountController = new AccountController(accountService);
        MockMvc mockMvc = standaloneSetup(accountController).build();

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice Smith\",\"email\":\"alice@example.com\",\"accountType\":\"SAVINGS\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.balance").value(0.0));

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.accountType").value("Savings"))
                .andExpect(jsonPath("$.balance").value(0.0));

        mockMvc.perform(post("/api/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.0));

        mockMvc.perform(post("/api/accounts/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(300.0));

        mockMvc.perform(get("/api/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].type").value("Deposit"))
                .andExpect(jsonPath("$[0].amount").value(500.0))
                .andExpect(jsonPath("$[0].date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].type").value("Withdrawal"))
                .andExpect(jsonPath("$[1].amount").value(200.0))
                .andExpect(jsonPath("$[1].date").value(LocalDate.now().toString()));
    }
}

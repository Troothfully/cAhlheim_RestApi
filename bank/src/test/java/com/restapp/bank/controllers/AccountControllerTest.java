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
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

class AccountControllerTest {

    @Test
    void accountFlowSupportsCreateDepositWithdrawAndHistory() throws Exception {
                AccountRepository accountRepository = mock(AccountRepository.class);
                TransactionRepository transactionRepository = mock(TransactionRepository.class);
                UserRepository userRepository = mock(UserRepository.class);

                List<Account> accounts = new ArrayList<>();
                List<Transaction> transactions = new ArrayList<>();

                when(userRepository.findById(1)).thenReturn(Optional.of(new User(1, "Alice Smith", "alice@example.com", "Admin")));

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
                        .content("{\"userId\":1,\"accountType\":\"SAVINGS\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
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
                .andExpect(content().string(org.hamcrest.Matchers.containsString("DEPOSIT")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("WITHDRAWAL")));
    }
}
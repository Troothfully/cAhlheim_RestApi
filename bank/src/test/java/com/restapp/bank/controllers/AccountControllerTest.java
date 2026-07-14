package com.restapp.bank.controllers;

import com.restapp.bank.repos.AccountRepository;
import com.restapp.bank.repos.TransactionRepository;
import com.restapp.bank.repos.UserRepository;
import com.restapp.bank.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AccountControllerTest {

    @Test
    void accountFlowSupportsCreateDepositWithdrawAndHistory() throws Exception {
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        UserRepository userRepository = new UserRepository();
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

        mockMvc.perform(post("/api/accounts/4/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.0));

        mockMvc.perform(post("/api/accounts/4/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(300.0));

        mockMvc.perform(get("/api/accounts/4/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("DEPOSIT")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("WITHDRAWAL")));
    }
}
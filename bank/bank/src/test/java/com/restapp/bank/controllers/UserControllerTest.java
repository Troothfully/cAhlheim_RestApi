package com.restapp.bank.controllers;

import com.restapp.bank.repos.UserRepository;
import com.restapp.bank.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class UserControllerTest {

    @Test
    void getAllUsersReturnsHardcodedUsers() throws Exception {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        MockMvc mockMvc = standaloneSetup(userController).build();

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Alice Smith")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bob Jones")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Charlie Brown")));
    }
}
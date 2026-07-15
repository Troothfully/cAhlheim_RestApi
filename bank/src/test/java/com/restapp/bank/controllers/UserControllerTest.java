package com.restapp.bank.controllers;

import com.restapp.bank.repos.UserRepository;
import com.restapp.bank.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.restapp.bank.models.User;

class UserControllerTest {

    @Test
    void getAllUsersReturnsHardcodedUsers() throws Exception {
    UserRepository userRepository = mock(UserRepository.class);
    when(userRepository.findAll()).thenReturn(List.of(
        new User(1, "Alice Smith", "alice@example.com", "Admin"),
        new User(2, "Bob Jones", "bob@example.com", "User"),
        new User(3, "Charlie Brown", "charlie@example.com", "User")
    ));
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        MockMvc mockMvc = standaloneSetup(userController).build();

    mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Alice Smith")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bob Jones")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Charlie Brown")));
    }
}
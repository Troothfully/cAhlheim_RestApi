package com.restapp.bank.services;

import org.springframework.stereotype.Service;

import com.restapp.bank.models.User;
import com.restapp.bank.repos.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    // Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        // Business logic can go here
        return userRepository.findAll();
    }
}
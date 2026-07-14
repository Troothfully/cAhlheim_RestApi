package com.restapp.bank.repos;

import org.springframework.stereotype.Repository;

import com.restapp.bank.models.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<User> userDatabase = new ArrayList<>();

    public UserRepository() {
        // Hardcoded Database
        userDatabase.add(new User(1L, "Alice Smith", "alice@example.com", "Admin"));
        userDatabase.add(new User(2L, "Bob Jones", "bob@example.com", "User"));
        userDatabase.add(new User(3L, "Charlie Brown", "charlie@example.com", "User"));
    }

    public List<User> findAll() {
        return userDatabase;
    }

    public User findById(Long id) {
        return userDatabase.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
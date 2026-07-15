package com.restapp.bank.models;

public record CreateAccountResponse(
        String id,
        String name,
        String email,
        String accountType,
        Double balance) {
}

package com.restapp.bank.models;

public record CreateAccountRequest(String name, String email, String accountType) {
}

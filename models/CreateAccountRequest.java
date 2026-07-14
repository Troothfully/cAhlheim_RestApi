package com.restapp.bank.models;

public record CreateAccountRequest(Long userId, String accountType) {
}
package com.restapp.bank.models;

public record TransactionResponse(String id, String type, Double amount, String date) {
}

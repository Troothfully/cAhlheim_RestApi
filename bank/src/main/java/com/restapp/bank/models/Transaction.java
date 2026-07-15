package com.restapp.bank.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "Transactions")
public class Transaction {
	private Integer id;
	private Integer accountId;
	private String transactionType;
	private Double amount;
	private LocalDate date;
	private String description;

	public Transaction(Integer id, Integer accountId, String transactionType, Double amount, LocalDate date, String description) {
		this.id = id;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.date = date;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

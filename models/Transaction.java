package com.restapp.bank.models;

public class Transaction {
	private Long id;
	private Long accountId;
	private String transactionType;
	private Double amount;
	private String description;

	public Transaction(Long id, Long accountId, String transactionType, Double amount, String description) {
		this.id = id;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

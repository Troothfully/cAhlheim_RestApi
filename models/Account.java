package com.restapp.bank.models;

public class Account {
	private Long id;
	private String accountNumber;
	private String accountType;
	private Double balance;
	private Long userId;

	public Account(Long id, String accountNumber, String accountType, Double balance, Long userId) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}

package com.restapp.bank.repos;

import com.restapp.bank.models.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {
	private final List<Transaction> transactionDatabase = new ArrayList<>();

	public TransactionRepository() {
		transactionDatabase.add(new Transaction(1L, 1L, "DEPOSIT", 500.00, "Initial deposit"));
		transactionDatabase.add(new Transaction(2L, 1L, "WITHDRAWAL", 75.25, "ATM withdrawal"));
		transactionDatabase.add(new Transaction(3L, 2L, "DEPOSIT", 1200.00, "Paycheck deposit"));
	}

	public List<Transaction> findAll() {
		return transactionDatabase;
	}

	public Transaction save(Transaction transaction) {
		transactionDatabase.add(transaction);
		return transaction;
	}

	public List<Transaction> findByAccountId(Long accountId) {
		return transactionDatabase.stream()
				.filter(transaction -> transaction.getAccountId().equals(accountId))
				.toList();
	}

	public Optional<Transaction> findLatestByAccountId(Long accountId) {
		return transactionDatabase.stream()
				.filter(transaction -> transaction.getAccountId().equals(accountId))
				.reduce((first, second) -> second);
	}
}

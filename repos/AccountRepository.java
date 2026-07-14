package com.restapp.bank.repos;

import com.restapp.bank.models.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Repository
public class AccountRepository {
	private final List<Account> accountDatabase = new ArrayList<>();

	public AccountRepository() {
		accountDatabase.add(new Account(1L, "ACC-1001", "Checking", 2450.75, 1L));
		accountDatabase.add(new Account(2L, "ACC-1002", "Savings", 9800.00, 2L));
		accountDatabase.add(new Account(3L, "ACC-1003", "Checking", 150.50, 3L));
	}

	public List<Account> findAll() {
		return accountDatabase;
	}

	public Account save(Account account) {
		accountDatabase.add(account);
		return account;
	}

	public Account findById(Long id) {
		return accountDatabase.stream()
				.filter(account -> account.getId().equals(id))
				.findFirst()
				.orElse(null);
	}

	public Optional<Account> findOptionalById(Long id) {
		return accountDatabase.stream()
				.filter(account -> account.getId().equals(id))
				.findFirst();
	}
}

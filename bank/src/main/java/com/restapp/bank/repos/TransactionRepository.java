package com.restapp.bank.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.restapp.bank.models.Transaction;

import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends MongoRepository<Transaction, Integer> {
	List<Transaction> findByAccountId(Integer accountId);
}

package com.restapp.bank.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.restapp.bank.models.Account;

@RepositoryRestResource
public interface AccountRepository extends MongoRepository<Account, Integer> {
}

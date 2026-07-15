package com.restapp.bank.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.restapp.bank.models.User;

import java.util.Optional;


@RepositoryRestResource
public interface UserRepository extends MongoRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}

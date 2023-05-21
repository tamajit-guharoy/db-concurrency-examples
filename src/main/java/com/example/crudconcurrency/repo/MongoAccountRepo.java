package com.example.crudconcurrency.repo;

import com.example.crudconcurrency.entity.Account;
import com.example.crudconcurrency.entity.MongoAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoAccountRepo extends MongoRepository<MongoAccount,Long> {
}

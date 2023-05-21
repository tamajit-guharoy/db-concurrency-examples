package com.example.crudconcurrency.repo;

import com.example.crudconcurrency.entity.MongoAccount;
import com.example.crudconcurrency.entity.RedisAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisAccountRepo extends CrudRepository<RedisAccount,Long> {
}

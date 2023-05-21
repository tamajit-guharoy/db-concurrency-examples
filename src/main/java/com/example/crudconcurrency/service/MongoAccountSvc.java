package com.example.crudconcurrency.service;

import com.example.crudconcurrency.entity.Account;
import com.example.crudconcurrency.entity.AccountVersioned;
import com.example.crudconcurrency.entity.MongoAccount;
import com.example.crudconcurrency.repo.AccountRepo;
import com.example.crudconcurrency.repo.AccountVersionedRepo;
import com.example.crudconcurrency.repo.MongoAccountRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

@Service
@AllArgsConstructor
//@NoArgsConstructor
@Data
public class MongoAccountSvc {
    private MongoAccountRepo accountRepo;

    @Transactional
    public void delete(Long id) {
         accountRepo.deleteById(id);
    }

    @Transactional
    public MongoAccount saveAccount(MongoAccount account) {
        MongoAccount save = accountRepo.save(account);
        return save;
    }

    @Transactional
    public MongoAccount findAccount(Long id) {
        return accountRepo.findById(id).orElseThrow();
    }

    @Transactional
    public MongoAccount incrementSalary(Long id, int increment) {
        MongoAccount account = accountRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.setSalary(account.getSalary() + increment);
        return accountRepo.save(account);
    }

}

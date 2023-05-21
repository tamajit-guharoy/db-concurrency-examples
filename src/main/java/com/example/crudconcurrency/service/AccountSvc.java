package com.example.crudconcurrency.service;

import com.example.crudconcurrency.entity.Account;
import com.example.crudconcurrency.entity.AccountVersioned;
import com.example.crudconcurrency.repo.AccountRepo;
import com.example.crudconcurrency.repo.AccountVersionedRepo;
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
public class AccountSvc {
    private AccountVersionedRepo accountVersionedRepo;
    private AccountRepo accountRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AccountVersioned saveAccountVersioned(AccountVersioned account) {
        AccountVersioned save = accountVersionedRepo.save(account);

        return save;
    }

    @Transactional
    public AccountVersioned findAccountVersioned(Long id) {
        return accountVersionedRepo.findById(id).orElseThrow();

    }




    @Transactional
    public AccountVersioned incrementVersionedSalary(Long id, int increment) {
        AccountVersioned save = accountVersionedRepo.findById(id).orElseThrow();
        save.setSalary(save.getSalary() + increment);
        accountVersionedRepo.save(save);
        return save;
    }
    @Transactional
    public Account saveAccount(Account account) {
        Account save = accountRepo.save(account);
        return save;
    }

    @Transactional
    public Account findAccount(Long id) {
        return accountRepo.findById(id).orElseThrow();
    }

    @Transactional
    public Account incrementSalary(Long id, int increment) {
//        Account account = accountRepo.findById(id).orElseThrow();
        Account account = entityManager.find(Account.class, id, LockModeType.PESSIMISTIC_WRITE);

        account.setSalary(account.getSalary() + increment);
        accountRepo.save(account);
        return account;
    }
   /* @Transactional
    public Account incrementSalary(Long id, int increment) {
        Account account = accountRepo.findById(id).orElseThrow();

        while (true) {
            try {
                account.setSalary(account.getSalary() + increment);
                accountRepo.save(account);
                break; // Exit the loop if the update succeeds without any optimistic locking conflict
            } catch (ObjectOptimisticLockingFailureException ex) {
                // Retry the operation by re-fetching the entity and attempting the update again
                account = accountRepo.findById(id).orElseThrow();
            }
        }

        return account;
    }*/


}

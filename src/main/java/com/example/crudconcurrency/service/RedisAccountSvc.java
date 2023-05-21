package com.example.crudconcurrency.service;

import com.example.crudconcurrency.entity.MongoAccount;
import com.example.crudconcurrency.entity.RedisAccount;
import com.example.crudconcurrency.repo.MongoAccountRepo;
import com.example.crudconcurrency.repo.RedisAccountRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
//@NoArgsConstructor
@Data
public class RedisAccountSvc {
    private RedisAccountRepo accountRepo;

    @Autowired
    private RedisTemplate<String, RedisAccount> redisTemplate;

    @Transactional
    public void delete(Long id) {
         accountRepo.deleteById(id);
    }

    @Transactional
    public RedisAccount saveAccount(RedisAccount account) {
        RedisAccount save = accountRepo.save(account);
        return save;
    }

    @Transactional
    public RedisAccount findAccount(Long id) {
        return accountRepo.findById(id).orElseThrow();
    }

    @Transactional
    public RedisAccount incrementSalaryOptimistic(Long id, int increment) {
        RedisAccount account = accountRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.setBalance(account.getBalance() + increment);
        return accountRepo.save(account);
    }

    @Transactional
    public RedisAccount incrementSalaryOptimistic2(String id, int increment) {
        RedisAccount account = redisTemplate.opsForValue().get(id);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        account.setBalance(account.getBalance() + increment);
        redisTemplate.opsForValue().set(id, account);

        return account;
    }


    @Transactional
    public RedisAccount incrementSalaryPessimistic(Long id, int increment) {
        RedisAccount account = accountRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

//        redisTemplate.opsForValue().setIfAbsent(getLockKey(id), true);

        try {
            redisTemplate.multi();

            account.setBalance(account.getBalance() + increment);
            accountRepo.save(account);

            redisTemplate.exec();
        } finally {
            redisTemplate.delete(getLockKey(id));
        }

        return account;
    }

    private String getLockKey(Long accountId) {
        return "lock:" + accountId;
    }
}

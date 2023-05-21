package com.example.crudconcurrency.service;

import com.example.crudconcurrency.entity.Account;
import com.example.crudconcurrency.entity.AccountVersioned;
import com.example.crudconcurrency.entity.MongoAccount;
import com.example.crudconcurrency.repo.AccountVersionedRepo;
import com.example.crudconcurrency.repo.MongoAccountRepo;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@SpringBootTest
class MongoAccountSvcTest {
    @Autowired
    private MongoAccountSvc accountSvc;

    @Autowired
    private MongoAccountRepo accountRepo;



    @BeforeEach
    public void setup() {
        // Perform any necessary setup before each test, such as initializing test data
    }

    @AfterEach
    public void cleanup() {
        // Perform any necessary cleanup after each test, such as deleting test data
    }

    @Test
//    @Transactional
    public void testOptimisticLock() throws InterruptedException {
        accountSvc.delete(1l);

        int requests = 200;
        CountDownLatch latch = new CountDownLatch(requests);
        MongoAccount account = new MongoAccount(1l, 0,null);

        MongoAccount savedAccount = accountSvc.saveAccount(account);
        long start = System.currentTimeMillis();
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + "incrementing...");

            while (true) {
                try {
                    accountSvc.incrementSalary(1l, 1);
                    break; // Exit the loop if the update succeeds without any optimistic locking conflict
                } catch (Exception ex) {
                    System.out.println(Thread.currentThread().getName() + "retrying...................................");
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            latch.countDown();
        };

        IntStream.range(0, requests).forEach((i) -> {
            new Thread(r).start();
        });

        latch.await();
        System.out.println("Time taken(ms):" + (System.currentTimeMillis() - start));
        System.out.println(accountSvc.findAccount(1l));
    }


}
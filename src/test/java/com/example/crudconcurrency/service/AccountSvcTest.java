package com.example.crudconcurrency.service;

import com.example.crudconcurrency.entity.Account;
import com.example.crudconcurrency.entity.AccountVersioned;
import com.example.crudconcurrency.repo.AccountVersionedRepo;
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
class AccountSvcTest {
    @Autowired
    private AccountSvc accountSvc;

    @Autowired
    private AccountVersionedRepo accountRepo;

    @Autowired
    private HikariDataSource dataSource;

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
        System.out.println("Threadpool size" + dataSource.getMaximumPoolSize());
        HikariPoolMXBean hikariPoolMXBean = dataSource.getHikariPoolMXBean();
        System.out.println("Total threads:" + hikariPoolMXBean.getTotalConnections());
        int requests = 200;
        CountDownLatch latch = new CountDownLatch(requests);
        AccountVersioned account = new AccountVersioned(1l, 0, 0l);

        AccountVersioned savedAccount = accountSvc.saveAccountVersioned(account);
        long start = System.currentTimeMillis();
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + "incrementing...");

            while (true) {
                try {
                    accountSvc.incrementVersionedSalary(1l, 1);
                    break; // Exit the loop if the update succeeds without any optimistic locking conflict
                } catch (ObjectOptimisticLockingFailureException ex) {
                    System.out.println(Thread.currentThread().getName() + "retrying...................................");
                    try {
                        Thread.sleep(new Random().nextInt(100));
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
        System.out.println(accountSvc.findAccountVersioned(1l));
    }

    @Test
//    @Transactional
    public void testPessimesticLocking() throws InterruptedException {
        System.out.println("Threadpool size" + dataSource.getMaximumPoolSize());
        HikariPoolMXBean hikariPoolMXBean = dataSource.getHikariPoolMXBean();
        System.out.println("Total threads:" + hikariPoolMXBean.getTotalConnections());
        int requests = 200;
        CountDownLatch latch = new CountDownLatch(requests);
        Account account = new Account(1l, 0);

        Account savedAccount = accountSvc.saveAccount(account);
        long start = System.currentTimeMillis();
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + "incrementing...");
            accountSvc.incrementSalary(1l, 1);
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
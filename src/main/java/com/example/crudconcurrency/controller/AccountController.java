package com.example.crudconcurrency.controller;

import com.example.crudconcurrency.entity.AccountVersioned;
import com.example.crudconcurrency.entity.MongoAccount;
import com.example.crudconcurrency.service.AccountSvc;
import com.example.crudconcurrency.service.MongoAccountSvc;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private AccountSvc accountSvc;
    private MongoAccountSvc mongoAccountSvc;

    @PostMapping(value = "/rdbms")

    public AccountVersioned saveAccount(@RequestBody AccountVersioned account) {
        AccountVersioned account1 = accountSvc.saveAccountVersioned(account);
        return account1;
    }

    @PostMapping(value = "/mongo")
    public MongoAccount saveMongoAccount(@RequestBody MongoAccount account) {
        MongoAccount account1 = mongoAccountSvc.saveAccount(account);
        return account1;
    }
}

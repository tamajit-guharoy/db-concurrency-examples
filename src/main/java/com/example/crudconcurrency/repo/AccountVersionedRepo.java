package com.example.crudconcurrency.repo;

import com.example.crudconcurrency.entity.AccountVersioned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountVersionedRepo extends JpaRepository<AccountVersioned,Long> {
}

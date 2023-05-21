package com.example.crudconcurrency.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("accounts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RedisAccount {
    @Id
    private String id;

    private double balance;
    @Version
    private int version;
    // Constructors, getters, and setters
}

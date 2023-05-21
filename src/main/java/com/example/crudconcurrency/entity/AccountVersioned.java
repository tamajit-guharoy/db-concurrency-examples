package com.example.crudconcurrency.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountVersioned {
    @Id
    private Long id;
    private int salary;
    @Version
    private Long version;
}

package com.example.crudconcurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"com.example.crudconcurrency"})
public class CrudConcurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudConcurrencyApplication.class, args);
	}

}

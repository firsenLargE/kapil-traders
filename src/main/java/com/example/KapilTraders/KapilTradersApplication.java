package com.example.KapilTraders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.example.KapilTraders", "controller", "model", "service", "repository", "ServiceImpl"})
@EntityScan(basePackages = {"com.example.KapilTraders", "model"})
@EnableJpaRepositories(basePackages = {"com.example.KapilTraders", "repository"})
public class KapilTradersApplication {

	public static void main(String[] args) {
		SpringApplication.run(KapilTradersApplication.class, args);
	}

}

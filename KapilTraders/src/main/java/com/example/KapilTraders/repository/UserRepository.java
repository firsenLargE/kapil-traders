package com.example.KapilTraders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.KapilTraders.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
   User findByEmailAndPassword(String email, String password);

}

package com.example.KapilTraders.service;

import com.example.KapilTraders.model.User;

public interface UserService {
    void signUp(User user);
    User login(String email, String password);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
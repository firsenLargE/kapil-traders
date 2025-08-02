package com.example.KapilTraders.service;

import com.example.KapilTraders.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {

    @Autowired

    void signUp(User user);
    User login(String email, String password);
}

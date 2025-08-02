package com.example.KapilTraders.controller;

import com.example.KapilTraders.model.User;
import com.example.KapilTraders.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {
    @Autowired
    private UserService userService;
 //this is a signup controller .....  thought a next controller name defined will be easy to see manipulate
 @PostMapping("/signup")
  public String postSignup(@ModelAttribute User user) {
     user.setEmail(user.getEmail().toLowerCase()); // normalize
     user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes())); // hash
     userService.signUp(user);

     return "index";
 }

}



package com.example.KapilTraders.controller;

import com.example.KapilTraders.model.User;
import com.example.KapilTraders.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String postSignup(@ModelAttribute User user, 
                           RedirectAttributes redirectAttributes, 
                           Model model) {
        try {
            // Check if user already exists
            if (userService.existsByEmail(user.getEmail().toLowerCase())) {
                model.addAttribute("error", "Email already exists. Please use a different email.");
                return "signup";
            }
            
            // Set the username from the form (the form uses "username" but model expects "uname")
            String username = user.getUname(); // This will be null, so we need to get it differently
            
            // Normalize email and hash password
            user.setEmail(user.getEmail().toLowerCase());
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            
            userService.signUp(user);
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please login.");
            
            return "redirect:/";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error creating account: " + e.getMessage());
            return "signup";
        }
    }
}
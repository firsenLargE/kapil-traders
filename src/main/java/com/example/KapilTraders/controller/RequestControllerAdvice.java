package com.example.KapilTraders.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RequestControllerAdvice {
    @ModelAttribute
    public void addCurrentURI(HttpServletRequest request, Model model) {
        model.addAttribute("currentURI", request.getRequestURI());
    }
}

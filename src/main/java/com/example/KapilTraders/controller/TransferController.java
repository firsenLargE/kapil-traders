package com.example.KapilTraders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.KapilTraders.service.TransferService;

@Controller
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("transfers", transferService.findAll());
        return "transfers";
    }
}

package com.example.KapilTraders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.KapilTraders.service.ReportService;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("stockReport",
                reportService.generateStockReport());
        model.addAttribute("lowStockCount",
                reportService.countLowStockProducts());
        model.addAttribute("totalProducts",
                reportService.countTotalProducts());
        model.addAttribute("pendingTransfers",
                reportService.countPendingTransfers());
        return "reports";
    }
}

package com.example.KapilTraders.controller;

import com.example.KapilTraders.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.KapilTraders.service.BarcodeService;

@Controller
@RequestMapping("/barcode-scan")
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    @GetMapping
    public String showScanForm(Model model) {
        model.addAttribute("barcodeData", "");
        model.addAttribute("product", null);
        return "barcode-scan";
    }

    @PostMapping
    public String handleScan(@RequestParam String barcodeData, Model model) {
        model.addAttribute("barcodeData", barcodeData);
        Product p = barcodeService.scanBarcode(barcodeData)
                .orElse(null);
        model.addAttribute("product", p);
        return "barcode-scan";
    }
}

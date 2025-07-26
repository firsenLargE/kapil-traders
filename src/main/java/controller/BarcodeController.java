package controller;

import service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BarcodeController {
    @Autowired
    private BarcodeService barcodeService;

    @GetMapping("/barcode-scan")
    public String scanBarcodeForm() {
        return "barcode-scan";
    }

    @PostMapping("/products/scan")
    public String scanBarcode(@RequestParam String barcode, Model model) {
        barcodeService.scanBarcode(barcode).ifPresent(product -> model.addAttribute("scannedProduct", product));
        return "barcode-scan";
    }
}

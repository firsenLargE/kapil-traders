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
    public String scanBarcodeForm(Model model) {
        return "barcode-scan";
    }

    @PostMapping("/barcode-scan")
    public String scanBarcode(@RequestParam String barcode, Model model) {
        try {
            // Validate barcode input
            if (barcode == null || barcode.trim().isEmpty()) {
                model.addAttribute("error", "Please enter a barcode to scan");
                return "barcode-scan";
            }
            
            // Clean the barcode input
            String cleanBarcode = barcode.trim();
            
            // Try to find the product
            var productOptional = barcodeService.scanBarcode(cleanBarcode);
            
            if (productOptional.isPresent()) {
                model.addAttribute("scannedProduct", productOptional.get());
                model.addAttribute("success", "Product found successfully!");
            } else {
                model.addAttribute("error", "No product found with barcode: " + cleanBarcode);
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Error scanning barcode: " + e.getMessage());
            System.err.println("Barcode scan error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "barcode-scan";
    }
}

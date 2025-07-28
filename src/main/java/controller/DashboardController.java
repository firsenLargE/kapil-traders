package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import service.ProductService;
import service.ReportService;

@Controller
public class DashboardController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    public String dashboard(Model model) {
        try {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("totalProducts", reportService.countTotalProducts());
            model.addAttribute("lowStockCount", reportService.countLowStockProducts());
            model.addAttribute("pendingTransfers", reportService.countPendingTransfers());
        } catch (Exception e) {
            model.addAttribute("products", java.util.Collections.emptyList());
            model.addAttribute("totalProducts", 0);
            model.addAttribute("lowStockCount", 0);
            model.addAttribute("pendingTransfers", 0);
            model.addAttribute("error", "Error loading dashboard data: " + e.getMessage());
        }
        return "index";
    }
}

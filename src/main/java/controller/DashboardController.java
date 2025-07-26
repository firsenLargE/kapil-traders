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
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("totalProducts", reportService.countTotalProducts());
        model.addAttribute("lowStockCount", reportService.countLowStockProducts());
        model.addAttribute("pendingTransfers", reportService.countPendingTransfers());
        return "index";  // this matches your index.html
    }
}

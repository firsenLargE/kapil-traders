package controller;

import service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/reports")
    public String generateReport(Model model) {
        model.addAttribute("reports", reportService.generateStockReport());
        return "reports";
    }
}

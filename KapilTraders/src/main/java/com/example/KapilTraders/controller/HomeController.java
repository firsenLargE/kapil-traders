package com.example.KapilTraders.controller;

import com.example.KapilTraders.model.Product;
import com.example.KapilTraders.model.User;
import com.example.KapilTraders.service.ProductService;
import com.example.KapilTraders.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }
        
        // Get dashboard statistics
        List<Product> products = productService.getAllProducts();
        long totalProducts = productService.countProducts();
        long lowStockProducts = productService.countLowStockProducts();
        double totalValue = productService.getTotalInventoryValue();
        
        // Get recent products (last 5)
        List<Product> recentProducts = products.stream()
            .sorted((p1, p2) -> p2.getDate() != null && p1.getDate() != null ? 
                p2.getDate().compareTo(p1.getDate()) : 0)
            .limit(5)
            .toList();
        
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("recentProducts", recentProducts);
        
        return "KTDashboard";
    }

    @GetMapping("/signup")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute User user, Model model, HttpSession session) {
        try {
            String hashedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            
            User usr = userService.login(user.getEmail().toLowerCase(), hashedPassword);
            
            if (usr != null) {
                session.setAttribute("validuser", usr);
                session.setMaxInactiveInterval(3600); // 1 hour session
                model.addAttribute("uname", usr.getUname());
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "index";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "index";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
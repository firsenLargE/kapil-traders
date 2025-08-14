package com.example.KapilTraders.controller;

import com.example.KapilTraders.model.Product;
import com.example.KapilTraders.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String showProducts(Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        populateSummary(model, products);
        return "products";
    }


    @GetMapping("/add-product")
    public String showAddProductForm(Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute Product product,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        try {
            if (product.getDate() == null) {
                product.setDate(LocalDate.now());
            }
            productService.addProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/edit-product/{id}")
    public String updateProduct(@PathVariable Integer id,
                                @ModelAttribute Product product,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        try {
            product.setId(id);
            productService.updateProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Integer id,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/search-products")
    public String searchProducts(@RequestParam String query, Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", query);
        populateSummary(model, products);
        return "products";
    }

    /** Populate common summary fields used by the products template. */
    private void populateSummary(Model model, List<Product> products) {
        model.addAttribute("totalProducts", products.size());
        long lowStock = products.stream()
                .filter(p -> safeQuantity(p) < 10)
                .count();
        model.addAttribute("lowStockProducts", lowStock);

        double totalValue = products.stream()
                .mapToDouble(p -> safePrice(p) * safeQuantity(p))
                .sum();
        model.addAttribute("totalValue", totalValue);
    }

    private int safeQuantity(Product p) {
        Integer q = p.getQuantity();
        return q != null ? q : 0;
    }

    private double safePrice(Product p) {
        Double price = p.getPrice();
        return price != null ? price : 0.0;
    }
}
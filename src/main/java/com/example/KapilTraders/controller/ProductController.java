package com.example.KapilTraders.controller;

import com.example.KapilTraders.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.KapilTraders.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // list all products
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    // show add form
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    // handle form submit
    @PostMapping("/add")
    public String save(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // search endpoint (optional)
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String category,
                         Model model) {
        model.addAttribute("products",
                productService.searchProducts(name, category));
        return "products";
    }

    // delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}

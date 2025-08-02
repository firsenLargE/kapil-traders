package com.example.KapilTraders.service;

import com.example.KapilTraders.model.Product;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);
    List<Product> getAllProducts();
    List<Product> searchProducts(String name);
    void deleteProduct(Integer id);
}

package com.example.KapilTraders.ServiceImpl;

import com.example.KapilTraders.model.Product;
import com.example.KapilTraders.repository.ProductRepository;
import com.example.KapilTraders.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    };
    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.findById(id).ifPresent(productRepository::delete);
    }
}

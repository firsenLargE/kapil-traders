package com.example.KapilTraders.ServiceImpl;

import com.example.KapilTraders.model.Product;
import com.example.KapilTraders.repository.ProductRepository;
import com.example.KapilTraders.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.findById(id).ifPresent(productRepository::delete);
    }

    @Override
    public long countProducts() {
        return productRepository.count();
    }

    @Override
    public long countLowStockProducts() {
        return productRepository.countByQuantityLessThan(10);
    }

    @Override
    public double getTotalInventoryValue() {
        List<Product> products = productRepository.findAll();
        return products.stream()
            .mapToDouble(p -> p.getPrice() * p.getQuantity())
            .sum();
    }
}
package com.example.KapilTraders.repository;

import com.example.KapilTraders.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByCategoryContainingIgnoreCase(String category);
    
    long countByQuantityLessThan(int quantity);
    
    List<Product> findByQuantityLessThan(int quantity);
    
    @Query("SELECT p FROM Product p ORDER BY p.date DESC")
    List<Product> findAllOrderByDateDesc();
    
    @Query("SELECT p FROM Product p WHERE p.quantity < ?1 ORDER BY p.quantity ASC")
    List<Product> findLowStockProducts(int threshold);
}
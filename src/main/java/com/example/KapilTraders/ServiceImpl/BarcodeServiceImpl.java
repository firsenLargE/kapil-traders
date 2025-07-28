package com.example.KapilTraders.ServiceImpl;
import com.example.KapilTraders.model.Product;
import com.example.KapilTraders.repository.ProductRepository;
import com.example.KapilTraders.service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BarcodeServiceImpl implements BarcodeService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<Product> scanBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }
}

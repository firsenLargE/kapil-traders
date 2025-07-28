package com.example.KapilTraders.service;


import com.example.KapilTraders.model.Product;
import java.util.Optional;

public interface BarcodeService {
    Optional<Product> scanBarcode(String barcode);
}

package com.example.KapilTraders.ServiceImpl;

import com.example.KapilTraders.model.Product;
import com.example.KapilTraders.repository.ProductRepository;
import com.example.KapilTraders.repository.TransferRepository;
import com.example.KapilTraders.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public List<Map<String, Object>> generateStockReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", p.getId());
            row.put("name", p.getName());
            row.put("quantity", p.getQuantity());
            report.add(row);
        }
        return report;
    }

    @Override
    public long countLowStockProducts() {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getQuantity() <= 10)
                .count();
    }

    @Override
    public long countTotalProducts() {
        return productRepository.count();
    }

    @Override
    public long countPendingTransfers() {
        return transferRepository.findAll()
                .stream()
                .filter(t -> "Pending".equals(t.getStatus()))
                .count();
    }

    @Override
    public Object generateAll() {
        return null;
    }
}

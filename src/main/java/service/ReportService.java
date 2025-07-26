package service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> generateStockReport();
    long countLowStockProducts();
    long countTotalProducts();
    long countPendingTransfers();
}
package ServiceImpl;

import model.Product;
import model.Transfer;
import repository.ProductRepository;
import repository.TransferRepository;
import service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransferRepository transferRepository;

    @Override
    public List<Map<String, Object>> generateStockReport() {
        List<Map<String, Object>> report = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("productName", product.getName());
            entry.put("category", product.getCategory());
            entry.put("quantity", product.getQuantity());
            entry.put("value", product.getQuantity() * product.getPrice());
            report.add(entry);
        }
        return report;
    }

    @Override
    public long countLowStockProducts() {
        return productRepository.findAll().stream().filter(p -> p.getQuantity() <= 10).count();
    }

    @Override
    public long countTotalProducts() {
        return productRepository.count();
    }

    @Override
    public long countPendingTransfers() {
        return transferRepository.findAll().stream().filter(t -> t.getStatus().equals("Pending")).count();
    }
}


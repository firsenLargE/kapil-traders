package service;

import model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> searchProducts(String name, String category);
    Optional<Product> getProductById(Long id);
    Optional<Product> getProductByBarcode(String barcode);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
}

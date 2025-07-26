package service;


import model.Product;
import java.util.Optional;

public interface BarcodeService {
    Optional<Product> scanBarcode(String barcode);
}

package com.example.KapilTraders.config;

import com.example.KapilTraders.model.Product;
import com.example.KapilTraders.model.User;
import com.example.KapilTraders.repository.ProductRepository;
import com.example.KapilTraders.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample user if no users exist
        if (userRepository.count() == 0) {
            User adminUser = new User();
            adminUser.setUname("admin");
            adminUser.setEmail("admin@kapiltraders.com");
            adminUser.setPassword(DigestUtils.md5DigestAsHex("admin123".getBytes()));
            userRepository.save(adminUser);
            
            System.out.println("Created default admin user:");
            System.out.println("Email: admin@kapiltraders.com");
            System.out.println("Password: admin123");
        }

        // Initialize sample products if no products exist
        if (productRepository.count() == 0) {
            // Sample products
            Product[] sampleProducts = {
                createProduct("Laptop", "Electronics", "15-inch", 25, 999.99),
                createProduct("Wireless Mouse", "Electronics", "Standard", 150, 29.99),
                createProduct("Office Chair", "Furniture", "Executive", 8, 199.99),
                createProduct("Notebook", "Stationery", "A4", 200, 2.50),
                createProduct("Desk Lamp", "Electronics", "LED", 45, 49.99),
                createProduct("Coffee Mug", "Kitchen", "320ml", 75, 12.99),
                createProduct("Pen Set", "Stationery", "Blue/Black", 5, 15.99), // Low stock
                createProduct("Monitor", "Electronics", "24-inch", 12, 299.99),
                createProduct("Keyboard", "Electronics", "Mechanical", 35, 89.99),
                createProduct("Water Bottle", "Kitchen", "500ml", 3, 8.99) // Low stock
            };

            for (Product product : sampleProducts) {
                productRepository.save(product);
            }
            
            System.out.println("Initialized " + sampleProducts.length + " sample products");
        }
    }
    
    private Product createProduct(String name, String category, String size, int quantity, double price) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setSize(size);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setDate(LocalDate.now().minusDays((long) (Math.random() * 30))); // Random date within last 30 days
        return product;
    }
}
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.jpa.repository.Query;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
// import java.util.Optional;
import java.util.stream.Collectors;

// ============ MAIN APPLICATION CLASS ============
@SpringBootApplication
public class KapilTradersApplication {
    public static void main(String[] args) {
        SpringApplication.run(KapilTradersApplication.class, args);
    }
}

// ============ ENTITIES ============




@Entity
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact;

    @OneToMany(mappedBy = "customer")
    private List<Sale> sales;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public List<Sale> getSales() { return sales; }
    public void setSales(List<Sale> sales) { this.sales = sales; }
}
@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uname;
    private String email;
    private String password;
    
    // Constructors
    public User() {}
    public User(String uname, String email, String password) {
        this.uname = uname;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUname() { return uname; }
    public void setUname(String uname) { this.uname = uname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

@Entity
@Table(name = "products")
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String category;
    private String size;
    private Integer quantity;
    private Double price;
    private LocalDate date;
    private String imagePath;
    // Constructors
    public Product() {}
    public Product(String name, String category, String size, Integer quantity, Double price, LocalDate date,String imagePath) {
        this.name = name;
        this.category = category;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
		this.imagePath = imagePath;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
	public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}

@Entity
@Table(name = "sales")
class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; 

    private Integer quantity;
    private Double totalPrice;
    private LocalDate date;

    public Sale() {}

    public Sale(Product product, Integer quantity, Double totalPrice, LocalDate date) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}

// ============ REPOSITORIES ============
@Repository
interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}

@Repository
interface ProductRepository extends org.springframework.data.jpa.repository.JpaRepository<Product, Integer> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryContainingIgnoreCase(String category);
    long countByQuantityLessThan(int quantity);
    List<Product> findByQuantityLessThan(int quantity);
    
    @Query("SELECT p FROM Product p ORDER BY p.date DESC")
    List<Product> findAllOrderByDateDesc();
	List<Product> findByImagePathIsNotNull();
    List<Product> findByImagePathIsNull();
}
@Repository
interface SaleRepository extends org.springframework.data.jpa.repository.JpaRepository<Sale, Integer> {
    List<Sale> findByDate(LocalDate date);
}

@Repository
interface CustomerRepository extends org.springframework.data.jpa.repository.JpaRepository<Customer, Long> {
	// List<Customer> findByNameContainingIgnoreCase(String name);	
}
// ============ SERVICES ============
interface UserService {
    void signUp(User user);
    User login(String email, String password);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}

interface ProductService {
    void addProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Integer id);
    void updateProduct(Product product);
    List<Product> searchProducts(String name);
    void deleteProduct(Integer id);
    long countProducts();
    long countLowStockProducts();
    double getTotalInventoryValue();
	

}
interface SaleService {
    void addSale(Sale sale);
    List<Sale> getAllSales();
    Sale getSaleById(Integer id); // <-- your method
}
interface CustomerService {
    void saveCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    void deleteCustomer(Long id);
}

@Service
class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void signUp(User user) {
        userRepository.save(user);
    }

    @Override
    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
    
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

@Service
class ProductServiceImpl implements ProductService {
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
@Service
class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public void addSale(Sale sale) {
        saleRepository.save(sale);
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Sale getSaleById(Integer id) {
        return saleRepository.findById(id).orElse(null);
    }
}


@Service
 class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }


	@Override
	public void deleteCustomer(Long id) {
		        customerRepository.deleteById(id);

	}
}

// ============ CONTROLLERS ============
@Controller
class HomeController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;

	@Autowired
	private SaleService saleService;


    @GetMapping("/")
    public String home() {
        return "index";
    }

   @GetMapping("/dashboard")
public String showDashboard(Model model, HttpSession session) {
    if (session.getAttribute("validuser") == null) {
        return "redirect:/";
    }
    
    // Dashboard statistics
    List<Product> products = productService.getAllProducts();
    long totalProducts = productService.countProducts();
    long lowStockProducts = productService.countLowStockProducts();
    double totalValue = productService.getTotalInventoryValue();
    
    // Recent products
    List<Product> recentProducts = products.stream()
            .sorted((p1, p2) -> p2.getDate() != null && p1.getDate() != null ? 
                p2.getDate().compareTo(p1.getDate()) : 0)
            .limit(5)
            .toList();
    
    // Optional: recent sales summary
    List<Sale> recentSales = saleService.getAllSales().stream().limit(5).toList();
    
    model.addAttribute("totalProducts", totalProducts);
    model.addAttribute("lowStockProducts", lowStockProducts);
    model.addAttribute("totalValue", totalValue);
    model.addAttribute("recentProducts", recentProducts);
    model.addAttribute("recentSales", recentSales);  // optional
    
    return "dashboard";
}

@GetMapping("/reports")
public String showReports(Model model, HttpSession session) {
    if (session.getAttribute("validuser") == null) return "redirect:/";

    List<Product> products = productService.getAllProducts();
    model.addAttribute("products", products);

    // Prepare arrays for chart.js
    List<String> productNames = products.stream()
                                        .map(Product::getName)
                                        .toList();
    List<Integer> productQuantities = products.stream()
                                              .map(Product::getQuantity)
                                              .map(q -> q != null ? q : 0)
                                              .toList();
    // Category distribution
    Map<String, Long> categoryCounts = products.stream()
            .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

    model.addAttribute("productNames", productNames);
    model.addAttribute("productQuantities", productQuantities);
    model.addAttribute("categoryCounts", categoryCounts);

    return "reports";
}



    @GetMapping("/signup")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute User user, Model model, HttpSession session) {
        try {
            User usr = userService.login(user.getEmail().toLowerCase(), user.getPassword());
            
            if (usr != null) {
                session.setAttribute("validuser", usr);
                session.setMaxInactiveInterval(3600); // 1 hour session
                model.addAttribute("uname", usr.getUname());
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "index";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "index";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/signup")
    public String postSignup(@ModelAttribute User user, 
                           RedirectAttributes redirectAttributes, 
                           Model model) {
        try {
            // Check if user already exists
            if (userService.existsByEmail(user.getEmail().toLowerCase())) {
                model.addAttribute("error", "Email already exists. Please use a different email.");
                return "signup";
            }
            
            // Normalize email
            user.setEmail(user.getEmail().toLowerCase());
            
            userService.signUp(user);
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please login.");
            
            return "redirect:/";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error creating account: " + e.getMessage());
            return "signup";
        }
    }
}

@Controller
class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String showProducts(Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        populateSummary(model, products);
        return "products";
    }

    @GetMapping("/add-product")
    public String showAddProductForm(Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        model.addAttribute("product", new Product());
        return "add-product";
    }

@PostMapping("/add-product")
public String addProduct(@ModelAttribute Product product,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         RedirectAttributes redirectAttributes,
                         HttpSession session) {
    if (session.getAttribute("validuser") == null) {
        return "redirect:/";
    }

    try {
        if (product.getDate() == null) {
            product.setDate(LocalDate.now());
        }

        // External upload directory (relative to where the app runs)
        String uploadDir = "uploads/";
        File uploadPath = new File(uploadDir);

        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // create if not exists
        }

        if (!imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Store relative URL path (for browser access)
            product.setImagePath("/uploads/" + fileName);
        }

        productService.addProduct(product);
        redirectAttributes.addFlashAttribute("success", "Product added successfully!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error adding product: " + e.getMessage());
    }

    return "redirect:/products";
}


    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/edit-product/{id}")
    public String updateProduct(@PathVariable Integer id,
                                @ModelAttribute Product product,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        try {
            product.setId(id);
            productService.updateProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Integer id,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/search-products")
    public String searchProducts(@RequestParam String query, Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) {
            return "redirect:/";
        }

        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", query);
        populateSummary(model, products);
        return "products";
    }

    private void populateSummary(Model model, List<Product> products) {
        model.addAttribute("totalProducts", products.size());
        long lowStock = products.stream()
                .filter(p -> safeQuantity(p) < 10)
                .count();
        model.addAttribute("lowStockProducts", lowStock);

        double totalValue = products.stream()
                .mapToDouble(p -> safePrice(p) * safeQuantity(p))
                .sum();
        model.addAttribute("totalValue", totalValue);
    }

    private int safeQuantity(Product p) {
        Integer q = p.getQuantity();
        return q != null ? q : 0;
    }

    private double safePrice(Product p) {
        Double price = p.getPrice();
        return price != null ? price : 0.0;
    }
}

@Controller
class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

	@Autowired
	private CustomerService customerService;

	// Ensure this service is defined
	// @Autowired
	// private UserService userService;

 @GetMapping("/sales")
public String showSalesForm(Model model, HttpSession session) {
    if (session.getAttribute("validuser") == null) {
        return "redirect:/";
    }
    
    model.addAttribute("sale", new Sale());        // Must exist
    model.addAttribute("products", productService.getAllProducts());
	model.addAttribute("customers", customerService.getAllCustomers());
    model.addAttribute("sales", saleService.getAllSales());
	model.addAttribute("customer", new Customer());

    
    return "sales";
}

    @GetMapping("/sales/add")
    public String showAddSaleForm(Model model, HttpSession session) {
        if (session.getAttribute("validuser") == null) return "redirect:/";

        model.addAttribute("sale", new Sale());
        model.addAttribute("products", productService.getAllProducts());
        return "add-sale";
    }

  @PostMapping("/sales/add")
public String addSale(@ModelAttribute Sale sale,
                      RedirectAttributes redirectAttributes,
                      HttpSession session) {

    if (session.getAttribute("validuser") == null) return "redirect:/";

    try {
        // Fetch full Product and Customer objects using IDs from the form
        Product product = productService.getProductById(sale.getProduct().getId());
        Customer customer = customerService.getCustomerById(sale.getCustomer().getId());

        if (product == null || sale.getQuantity() > product.getQuantity()) {
            redirectAttributes.addFlashAttribute("error", "Invalid product or insufficient stock");
            return "redirect:/sales";
        }

        // Set the fetched objects to the sale
        sale.setProduct(product);
        sale.setCustomer(customer);

        // Reduce product stock
        product.setQuantity(product.getQuantity() - sale.getQuantity());
        productService.updateProduct(product);

        // Calculate total price
        sale.setTotalPrice(product.getPrice() * sale.getQuantity());
        sale.setDate(LocalDate.now());

        saleService.addSale(sale);
        redirectAttributes.addFlashAttribute("success", "Sale added successfully!");

    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error adding sale: " + e.getMessage());
    }

    return "redirect:/sales";
}


    @GetMapping("/sales/{id}/receipt")
    public void generateReceipt(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Sale sale = saleService.getSaleById(id);

        if (sale == null) return;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt_" + id + ".pdf");

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            document.add(new com.itextpdf.text.Paragraph("----- Invoice Kapil-----"));
            document.add(new com.itextpdf.text.Paragraph("Sale ID: " + sale.getId()));
            document.add(new com.itextpdf.text.Paragraph("Product: " + sale.getProduct().getName()));
            document.add(new com.itextpdf.text.Paragraph("Quantity: " + sale.getQuantity()));
            document.add(new com.itextpdf.text.Paragraph("Price per unit: " + sale.getProduct().getPrice()));
            document.add(new com.itextpdf.text.Paragraph("Total Price: " + sale.getTotalPrice()));
            document.add(new com.itextpdf.text.Paragraph("Date: " + sale.getDate()));
            document.add(new com.itextpdf.text.Paragraph("--------------------------"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}

@Controller
@RequestMapping("/customers")
 class CustomerController {

    @Autowired
    private CustomerService customerService;

    // List all customers
    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("customer", new Customer()); // For the Add form
        return "customers"; // Matches your customers.html
    }
    @GetMapping("/add") // URL: /customers/add
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers"; // Thymeleaf template
    }

    @PostMapping("/add") // URL: /customers/add
    public String saveOrUpdateCustomer(@ModelAttribute Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/customers"; // redirect to the customer list
    }

    // Show edit form (populates the same customers.html)
    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer); // Populates the form
        model.addAttribute("customers", customerService.getAllCustomers()); // Table data
        return "customers"; // Reuse the same template
    }

    // Delete customer
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}



// ============ DATA INITIALIZER ============
@Component
class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default admin user if no users exist
        if (userRepository.count() == 0) {
            User adminUser = new User();
            adminUser.setUname("admin");
            adminUser.setEmail("admin@kapiltraders.com");
            adminUser.setPassword("admin123"); // In a real app, hash this password
            userRepository.save(adminUser);

            System.out.println("Created default admin user:");
            System.out.println("Email: admin@kapiltraders.com");
            System.out.println("Password: admin123");
        }
        
        // No sample products will be created
    }
}
// WebConfig moved to its own file WebConfig.java

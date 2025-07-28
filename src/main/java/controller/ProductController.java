package controller;

import model.Product;
import model.ProductCategory;
import service.ProductService;
import service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    // List products
    @GetMapping
    public String listProducts(@RequestParam(required = false) String search,
                               @RequestParam(required = false) String category,
                               Model model) {
        try {
            model.addAttribute("products", productService.searchProducts(search, category));
            model.addAttribute("categories", productCategoryService.getActiveCategories());
        } catch (Exception e) {
            model.addAttribute("products", java.util.Collections.emptyList());
            model.addAttribute("categories", java.util.Collections.emptyList());
            model.addAttribute("error", "Error loading products: " + e.getMessage());
        }
        return "products";
    }

    // Show product selection cards for adding inventory
    @GetMapping("/add")
    public String selectProductForInventory(Model model) {
        try {
            // Get all existing products to show as cards
            model.addAttribute("existingProducts", productService.getAllProducts());
            model.addAttribute("categories", productCategoryService.getActiveCategories());
        } catch (Exception e) {
            model.addAttribute("existingProducts", java.util.Collections.emptyList());
            model.addAttribute("categories", java.util.Collections.emptyList());
            model.addAttribute("error", "Error loading products: " + e.getMessage());
        }
        return "select-product";
    }

    // Show add new product form (for completely new products)
    @GetMapping("/add/new")
    public String addNewProductForm(Model model) {
        try {
            model.addAttribute("categories", productCategoryService.getActiveCategories());
            model.addAttribute("product", new Product());
        } catch (Exception e) {
            model.addAttribute("categories", java.util.Collections.emptyList());
            model.addAttribute("error", "Error loading categories: " + e.getMessage());
        }
        return "add-new-product";
    }

    // Show add product form for specific category
    @GetMapping("/add/category/{categoryId}")
    public String addProductForm(@PathVariable Long categoryId, Model model) {
        try {
            ProductCategory category = productCategoryService.getCategoryById(categoryId).orElseThrow();
            Product product = new Product();
            product.setCategory(category.getName());
            model.addAttribute("product", product);
            model.addAttribute("category", category);
        } catch (Exception e) {
            model.addAttribute("error", "Category not found");
            return "redirect:/products/add";
        }
        return "add-product";
    }

    // Show add inventory form for existing product
    @GetMapping("/add/inventory/{productId}")
    public String addInventoryForm(@PathVariable Long productId, Model model) {
        try {
            Product existingProduct = productService.getProductById(productId).orElseThrow();
            Product newInventory = new Product();
            // Copy basic details from existing product
            newInventory.setName(existingProduct.getName());
            newInventory.setCategory(existingProduct.getCategory());
            newInventory.setVariant(existingProduct.getVariant());
            newInventory.setPrice(existingProduct.getPrice());
            newInventory.setSellingRate(existingProduct.getSellingRate());
            newInventory.setSupplier(existingProduct.getSupplier());
            newInventory.setDescription(existingProduct.getDescription());
            newInventory.setImageUrl(existingProduct.getImageUrl());
            
            model.addAttribute("product", newInventory);
            model.addAttribute("existingProduct", existingProduct);
        } catch (Exception e) {
            model.addAttribute("error", "Product not found");
            return "redirect:/products/add";
        }
        return "add-inventory";
    }

    // Save new product with image upload
    @PostMapping("/add/category/{categoryId}")
    public String saveProduct(@PathVariable Long categoryId, 
                             @ModelAttribute("product") Product product,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             RedirectAttributes redirectAttributes) {
        try {
            ProductCategory category = productCategoryService.getCategoryById(categoryId).orElseThrow();
            product.setCategory(category.getName());
            
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = handleImageUpload(imageFile);
                product.setImageUrl(imageUrl);
            }
            
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Save new inventory for existing product
    @PostMapping("/add/inventory/{productId}")
    public String saveInventory(@PathVariable Long productId, 
                               @ModelAttribute("product") Product product,
                               RedirectAttributes redirectAttributes) {
        try {
            Product existingProduct = productService.getProductById(productId).orElseThrow();
            // Copy basic details from existing product
            product.setName(existingProduct.getName());
            product.setCategory(existingProduct.getCategory());
            product.setVariant(existingProduct.getVariant());
            product.setPrice(existingProduct.getPrice());
            product.setSellingRate(existingProduct.getSellingRate());
            product.setSupplier(existingProduct.getSupplier());
            product.setDescription(existingProduct.getDescription());
            product.setImageUrl(existingProduct.getImageUrl());
            
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Inventory added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving inventory: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Save completely new product with image upload
    @PostMapping("/add/new")
    public String saveNewProduct(@ModelAttribute("product") Product product,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = handleImageUpload(imageFile);
                product.setImageUrl(imageUrl);
            }
            
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Show edit product form
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductById(id).orElseThrow();
            model.addAttribute("product", product);
        } catch (Exception e) {
            model.addAttribute("error", "Product not found");
            return "redirect:/products";
        }
        return "edit-product";
    }

    // Update product with image upload
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, 
                               @ModelAttribute Product product,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        try {
            product.setId(id);
            
            // Handle image upload if new image is provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = handleImageUpload(imageFile);
                product.setImageUrl(imageUrl);
            }
            
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Handle image upload
    private String handleImageUpload(MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file to upload");
        }
        
        // Check file size (max 2MB)
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 2MB");
        }
        
        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && 
                                   !contentType.equals("image/jpg") && 
                                   !contentType.equals("image/png") && 
                                   !contentType.equals("image/webp"))) {
            throw new IllegalArgumentException("Only JPG, PNG, and WebP images are allowed");
        }
        
        // Create upload directory if it doesn't exist
        String uploadDir = "uploads/products";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String filename = UUID.randomUUID().toString() + fileExtension;
        
        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);
        
        // Return the file path for storage in database
        return "/uploads/products/" + filename;
    }
}

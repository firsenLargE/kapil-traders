package controller;

import model.ProductCategory;
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
@RequestMapping("/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    // List categories
    @GetMapping
    public String listCategories(Model model) {
        try {
            model.addAttribute("categories", productCategoryService.getActiveCategories());
        } catch (Exception e) {
            model.addAttribute("categories", java.util.Collections.emptyList());
            model.addAttribute("error", "Error loading categories: " + e.getMessage());
        }
        return "categories";
    }

    // Show add category form
    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new ProductCategory());
        return "add-category";
    }

    // Save category with image upload
    @PostMapping("/add")
    public String saveCategory(@ModelAttribute ProductCategory category, 
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = handleImageUpload(imageFile);
                category.setImageUrl(imageUrl);
            }
            
            productCategoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving category: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    // Show edit category form
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        try {
            ProductCategory category = productCategoryService.getCategoryById(id).orElseThrow();
            model.addAttribute("category", category);
        } catch (Exception e) {
            model.addAttribute("error", "Category not found");
            return "redirect:/categories";
        }
        return "edit-category";
    }

    // Update category
    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, 
                                @ModelAttribute ProductCategory category,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {
        try {
            category.setId(id);
            
            // Handle image upload if new image is provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = handleImageUpload(imageFile);
                category.setImageUrl(imageUrl);
            }
            
            productCategoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating category: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    // Delete category
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productCategoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting category: " + e.getMessage());
        }
        return "redirect:/categories";
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
        String uploadDir = "uploads/categories";
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
        return "/uploads/categories/" + filename;
    }
} 
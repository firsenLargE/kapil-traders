package service;

import model.ProductCategory;
import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {
    List<ProductCategory> getAllCategories();
    List<ProductCategory> getActiveCategories();
    Optional<ProductCategory> getCategoryById(Long id);
    ProductCategory saveCategory(ProductCategory category);
    void deleteCategory(Long id);
    List<ProductCategory> searchCategories(String name);
} 
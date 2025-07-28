package ServiceImpl;

import model.ProductCategory;
import repository.ProductCategoryRepository;
import service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public List<ProductCategory> getActiveCategories() {
        return productCategoryRepository.findByActiveTrue();
    }

    @Override
    public Optional<ProductCategory> getCategoryById(Long id) {
        return productCategoryRepository.findById(id);
    }

    @Override
    public ProductCategory saveCategory(ProductCategory category) {
        return productCategoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public List<ProductCategory> searchCategories(String name) {
        return productCategoryRepository.findByNameContainingIgnoreCase(name);
    }
} 
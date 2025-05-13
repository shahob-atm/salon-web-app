package com.sh32bit.service.impl;

import com.sh32bit.model.Category;
import com.sh32bit.repository.CategoryRepository;
import com.sh32bit.request.CategoryRequest;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest req, SalonResponse res) {
        Category category = Category.builder()
                .name(req.name())
                .image(req.image())
                .salonId(res.getId())
                .build();

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Set<Category> getCategoriesBySalon(Long id) {
        return categoryRepository.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));
    }

    @Override
    public Category updateCategory(Long id, CategoryRequest req) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));
        category.setName(req.name());
        category.setImage(req.image());

        return null;
    }

    @Override
    public String deleteCategory(Long id) {
        categoryRepository.deleteById(id);

        return "Category deleted";
    }
}

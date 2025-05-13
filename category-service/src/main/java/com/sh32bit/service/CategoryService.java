package com.sh32bit.service;

import com.sh32bit.model.Category;
import com.sh32bit.request.CategoryRequest;
import com.sh32bit.response.SalonResponse;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    Category createCategory(CategoryRequest categoryRequest, SalonResponse res);

    List<Category> getCategories();

    Set<Category> getCategoriesBySalon(Long id);

    Category getCategoryById(Long id) throws Exception;

    Category updateCategory(Long id, CategoryRequest req) throws Exception;

    String deleteCategory(Long id);
}

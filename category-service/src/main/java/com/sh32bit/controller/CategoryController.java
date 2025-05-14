package com.sh32bit.controller;

import com.sh32bit.mapper.CategoryMapper;
import com.sh32bit.model.Category;
import com.sh32bit.request.CategoryRequest;
import com.sh32bit.response.CategoryResponse;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        SalonResponse res = SalonResponse.builder().id(1L).build();
        Category category = categoryService.createCategory(categoryRequest, res);

        CategoryResponse response = CategoryMapper.toCategoryResponse(category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        List<CategoryResponse> res = categories.stream().map(CategoryMapper::toCategoryResponse).toList();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<CategoryResponse>> getCategoriesBySalon(@PathVariable("id") Long id) {
        Set<Category> categories = categoryService.getCategoriesBySalon(id);
        Set<CategoryResponse> res = categories.stream().map(CategoryMapper::toCategoryResponse).collect(Collectors.toSet());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id) throws Exception {
        Category category = categoryService.getCategoryById(id);
        CategoryResponse response = CategoryMapper.toCategoryResponse(category);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("id") Long id,
                                                           @RequestBody CategoryRequest req) throws Exception {
        Category category = categoryService.updateCategory(id, req);
        CategoryResponse response = CategoryMapper.toCategoryResponse(category);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) throws Exception {
        String res = categoryService.deleteCategory(id);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

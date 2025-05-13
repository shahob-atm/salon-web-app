package com.sh32bit.mapper;

import com.sh32bit.model.Category;
import com.sh32bit.response.CategoryResponse;

public class CategoryMapper {
    public static CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .image(category.getImage())
                .salonId(category.getSalonId())
                .build();
    }
}

package com.sh32bit.service.client;

import com.sh32bit.response.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryFeignClient {
    @GetMapping("/api/categories/{id}")
    ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id) throws Exception;
}

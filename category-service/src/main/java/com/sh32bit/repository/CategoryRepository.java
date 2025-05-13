package com.sh32bit.repository;

import com.sh32bit.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> findBySalonId(Long id);
}

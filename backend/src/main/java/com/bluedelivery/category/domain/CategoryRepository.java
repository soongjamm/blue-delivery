package com.bluedelivery.category.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    long deleteCategoryById(Long id);
    
    List<Category> findCategoriesByIdIn(List<Long> categoryIds);
}

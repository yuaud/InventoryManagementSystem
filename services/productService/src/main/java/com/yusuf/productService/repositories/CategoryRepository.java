package com.yusuf.productService.repositories;

import com.yusuf.productService.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

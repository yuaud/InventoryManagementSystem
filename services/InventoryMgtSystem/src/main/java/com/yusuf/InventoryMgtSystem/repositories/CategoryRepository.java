package com.yusuf.InventoryMgtSystem.repositories;

import com.yusuf.InventoryMgtSystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

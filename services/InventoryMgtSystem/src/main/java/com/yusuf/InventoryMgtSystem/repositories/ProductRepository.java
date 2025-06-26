package com.yusuf.InventoryMgtSystem.repositories;

import com.yusuf.InventoryMgtSystem.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingOrDescriptionContaining(String productName, String productDescription);
}

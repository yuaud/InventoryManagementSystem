package com.yusuf.transactionService.repositories;

import com.yusuf.transactionService.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingOrDescriptionContaining(String productName, String productDescription);
}

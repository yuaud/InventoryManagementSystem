package com.yusuf.transactionService.repositories;

import com.yusuf.transactionService.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}

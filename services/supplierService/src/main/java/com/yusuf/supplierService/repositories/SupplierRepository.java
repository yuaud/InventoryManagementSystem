package com.yusuf.supplierService.repositories;

import com.yusuf.supplierService.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}

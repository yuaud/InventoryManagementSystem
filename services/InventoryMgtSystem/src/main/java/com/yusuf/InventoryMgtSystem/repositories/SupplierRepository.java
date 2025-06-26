package com.yusuf.InventoryMgtSystem.repositories;

import com.yusuf.InventoryMgtSystem.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}

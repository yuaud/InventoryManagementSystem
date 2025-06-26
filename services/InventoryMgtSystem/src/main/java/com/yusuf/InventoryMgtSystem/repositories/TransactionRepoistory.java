package com.yusuf.InventoryMgtSystem.repositories;

import com.yusuf.InventoryMgtSystem.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepoistory extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
}

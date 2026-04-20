package com.bar.bms.repository;

import com.bar.bms.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    // Standard CRUD methods are automatically included
}
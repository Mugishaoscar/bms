package com.bar.bms.repository;

import com.bar.bms.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByOrderByDateTimeDesc();
    @Query("SELECT SUM(s.totalAmount) FROM Sale s")
    Double getTotalSalesAmount();
}
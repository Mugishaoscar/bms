package com.bar.bms.repository;

import com.bar.bms.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAllByOrderByDateTimeDesc();

    @Query("SELECT SUM(s.totalAmount) FROM Sale s")
    Double getTotalSalesAmount();

    List<Sale> findByWorkerIdOrderByDateTimeDesc(Long workerId);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.workerId = :workerId")
    Double getTotalSalesAmountByWorkerId(@Param("workerId") Long workerId);
}
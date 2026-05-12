package com.bar.bms.repository;

import com.bar.bms.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {

    List<Debt> findAllByOrderByDateTimeDesc();

    List<Debt> findByWorkerIdOrderByDateTimeDesc(Long workerId);
    List<Debt> findByBossIdOrderByDateTimeDesc(Long bossId);
}
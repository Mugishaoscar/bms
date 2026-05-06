package com.bar.bms.repository;

import com.bar.bms.model.DayReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DayReportRepository extends JpaRepository<DayReport, Long> {
    List<DayReport> findAllByOrderBySubmittedAtDesc();

}
package com.bar.bms.repository;

import com.bar.bms.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // This custom query finds only the employees belonging to a specific boss
    List<Employee> findByBossId(Long bossId);
}
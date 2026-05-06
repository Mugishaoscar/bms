package com.bar.bms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class DayReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workerId;
    private Long bossId;

    private LocalDate reportDate;
    private Double totalSales;
    private LocalDateTime submittedAt;

    private String status; // SUBMITTED, REVIEWED
}
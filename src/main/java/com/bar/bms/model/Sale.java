package com.bar.bms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerPhone;
    private String customerEmail;

    private Double totalAmount;
    private LocalDateTime dateTime;

    private Long workerId;
    private Long bossId;
}
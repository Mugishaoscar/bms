package com.bar.bms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String position; // e.g., "Waiter", "Manager"
    private String phone;
    private String username;
    private String password;

    // IMPORTANT: This links the employee to the boss who created them
    private Long bossId;
}
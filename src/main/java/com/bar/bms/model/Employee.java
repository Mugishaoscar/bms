package com.bar.bms.model;

import jakarta.persistence.*;
import lombok.Data;

@Data // <--- This MUST be here to generate getBossId() and setBossId()
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String position; // Added this
    private String phone;    // Added this
    private String username;
    private String password;
    private Long bossId;
}
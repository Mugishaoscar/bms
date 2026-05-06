package com.bar.bms.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users") // "user" is a reserved word in many databases, so we use "users"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bossId;
    private String username;
    private String password;
    private String role; // This will store "BOSS"
}
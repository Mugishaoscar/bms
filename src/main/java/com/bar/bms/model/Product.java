package com.bar.bms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "products")
@Data // This automatically creates Getters and Setters
@Getter
@Setter

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bossId;
    private String name;
    private Double price;
    private Integer stockQuantity;
    private String imageUrl; // This will store the link to your beer images
}

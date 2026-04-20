package com.bar.bms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int quantity;
    private double pricePerUnit;
    private LocalDateTime saleDate;

    // Default Constructor
    public Sale() {}

    // Getters and helper for total
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getPricePerUnit() { return pricePerUnit; }
    public double getAmount() { return quantity * pricePerUnit; }
    public LocalDateTime getSaleDate() { return saleDate; }
}
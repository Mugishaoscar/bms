package com.bar.bms.model;

import jakarta.persistence.*;

@Entity // Tells MySQL to create a table named 'debt'
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String items;
    private String phone;
    private Double amount;

    // Standard Getters and Setters (Important if you aren't using Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
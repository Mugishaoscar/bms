package com.bar.bms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "debts")
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String items;
    private String phone;
    private Double amount;

    @Column(name = "date_recorded")
    private LocalDateTime dateRecorded;

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getItems() { return items; }
    public String getPhone() { return phone; }
    public Double getAmount() { return amount; }
    public LocalDateTime getDateRecorded() { return dateRecorded; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setItems(String items) { this.items = items; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAmount(Double amount) { this.amount = amount; }
}
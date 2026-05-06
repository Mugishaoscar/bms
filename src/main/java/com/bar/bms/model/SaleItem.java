package com.bar.bms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long saleId;
    private Long productId;

    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}
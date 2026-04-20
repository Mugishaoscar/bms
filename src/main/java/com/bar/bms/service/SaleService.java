package com.bar.bms.service;

import com.bar.bms.model.Sale;
import com.bar.bms.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SaleService {
    @Autowired
    private SaleRepository repository;

    public List<Sale> getAllDailySales() {
        return repository.findAll(); // Fetches data recorded during daily transactions
    }

    public double calculateTotalRevenue(List<Sale> sales) {
        return sales.stream().mapToDouble(Sale::getAmount).sum();
    }
}
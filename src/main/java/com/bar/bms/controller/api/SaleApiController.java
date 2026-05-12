package com.bar.bms.controller.api;

import com.bar.bms.model.Sale;
import com.bar.bms.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleApiController {

    @Autowired
    private SaleRepository saleRepository;

    @GetMapping
    public List<Sale> getAllSales() {
        return saleRepository.findAllByOrderByDateTimeDesc();
    }
}
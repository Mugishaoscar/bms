package com.bar.bms.controller.api;

import com.bar.bms.model.Debt;
import com.bar.bms.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtApiController {

    @Autowired
    private DebtRepository debtRepository;

    @GetMapping
    public List<Debt> getAllDebts() {
        return debtRepository.findAllByOrderByDateTimeDesc();
    }
}
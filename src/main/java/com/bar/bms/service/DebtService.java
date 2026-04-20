package com.bar.bms.service;

import com.bar.bms.model.Debt;
import com.bar.bms.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DebtService {
    @Autowired private DebtRepository repository;
    public List<Debt> getAllDebts() { return repository.findAll(); }
    public double calculateTotalDebt(List<Debt> debts) {
        return debts.stream().mapToDouble(Debt::getAmount).sum();
    }
}
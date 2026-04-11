package com.bar.bms.service;

import com.bar.bms.model.Debt;
import com.bar.bms.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DebtService {
    @Autowired
    private DebtRepository debtRepository;

    public List<Debt> getAllDebts() {
        return debtRepository.findAll();
    }

    public void saveDebt(Debt debt) {
        debtRepository.save(debt);
    }
}
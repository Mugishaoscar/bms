package com.bar.bms.service;

import com.bar.bms.model.Debt;
import com.bar.bms.model.Product;
import com.bar.bms.repository.DebtRepository;
import com.bar.bms.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private ProductRepository productRepository;

    public void createDebt(String customerName,
                           String customerPhone,
                           String customerEmail,
                           Long[] productIds,
                           Integer[] quantities,
                           Long workerId,
                           Long bossId) {

        double total = 0.0;
        StringBuilder itemsText = new StringBuilder();

        for (int i = 0; i < productIds.length; i++) {
            Product product = productRepository.findById(productIds[i]).orElse(null);

            if (product != null && product.getStockQuantity() >= quantities[i]) {
                double subtotal = product.getPrice() * quantities[i];

                product.setStockQuantity(product.getStockQuantity() - quantities[i]);
                productRepository.save(product);

                itemsText.append(product.getName())
                        .append(" x")
                        .append(quantities[i])
                        .append(", ");

                total += subtotal;
            }
        }

        Debt debt = new Debt();
        debt.setCustomerName(customerName);
        debt.setCustomerPhone(customerPhone);
        debt.setCustomerEmail(customerEmail);
        debt.setItems(itemsText.toString());
        debt.setTotalAmount(total);
        debt.setDateTime(LocalDateTime.now());
        debt.setWorkerId(workerId);
        debt.setBossId(bossId);
        debt.setStatus("UNPAID");

        debtRepository.save(debt);
    }
}
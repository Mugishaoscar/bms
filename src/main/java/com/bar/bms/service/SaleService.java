package com.bar.bms.service;

import com.bar.bms.model.Product;
import com.bar.bms.model.Sale;
import com.bar.bms.model.SaleItem;
import com.bar.bms.repository.ProductRepository;
import com.bar.bms.repository.SaleItemRepository;
import com.bar.bms.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public void createSale(String customerName,
                           String customerPhone,
                           String customerEmail,
                           Long[] productIds,
                           Integer[] quantities,
                           Long workerId,
                           Long bossId) {

        Sale sale = new Sale();
        sale.setCustomerName(customerName);
        sale.setCustomerPhone(customerPhone);
        sale.setCustomerEmail(customerEmail);
        sale.setDateTime(LocalDateTime.now());
        sale.setTotalAmount(0.0);
        sale.setWorkerId(workerId);
        sale.setBossId(bossId);

        Sale savedSale = saleRepository.save(sale);

        double total = 0.0;

        for (int i = 0; i < productIds.length; i++) {

            Product product = productRepository.findById(productIds[i]).orElse(null);

            if (product != null && product.getStockQuantity() >= quantities[i]) {

                double subtotal = product.getPrice() * quantities[i];

                product.setStockQuantity(product.getStockQuantity() - quantities[i]);
                productRepository.save(product);

                SaleItem item = new SaleItem();
                item.setSaleId(savedSale.getId());
                item.setProductId(product.getId());
                item.setQuantity(quantities[i]);
                item.setUnitPrice(product.getPrice());
                item.setSubtotal(subtotal);

                saleItemRepository.save(item);

                total += subtotal;
            }
        }

        savedSale.setTotalAmount(total);
        saleRepository.save(savedSale);
    }
}
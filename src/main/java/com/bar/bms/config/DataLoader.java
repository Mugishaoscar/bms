package com.bar.bms.config;

import com.bar.bms.model.Product;
import com.bar.bms.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) { // Only add if the table is empty
                Product p1 = new Product();
                p1.setName("Primus (600ml)");
                p1.setPrice(1200.0);
                p1.setStockQuantity(24);
                p1.setImageUrl("primus.png");

                Product p2 = new Product();
                p2.setName("Skol Malt");
                p2.setPrice(1000.0);
                p2.setStockQuantity(12);
                p2.setImageUrl("skol.png");

                repository.save(p1);
                repository.save(p2);
                System.out.println("Sample Data Loaded!");
            }
        };
    }
}
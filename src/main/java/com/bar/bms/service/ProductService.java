package com.bar.bms.service;

import com.bar.bms.model.Product;
import com.bar.bms.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Tells Spring this is the "Brain" of the app
public class ProductService {

    @Autowired // Automatically connects the Repository (the Database Bridge)
    private ProductRepository productRepository;

    // Method to get all beers from the database
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Method to save a new beer (or update an existing one)
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
}
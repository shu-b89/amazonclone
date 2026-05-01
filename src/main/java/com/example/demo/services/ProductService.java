package com.example.demo.services;

import com.example.demo.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> findAll();

    List<Product> findByCategory(String category);

    Product findById(int id);

    List<Product> search(String keyword);
    
    List<Product> findByBadge(String badge);
}
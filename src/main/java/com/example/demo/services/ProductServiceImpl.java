package com.example.demo.services;

import com.example.demo.entity.Product;
import com.example.demo.repoository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductRepository productRepository1;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> search(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    @Override
    public List<Product> findByBadge(String badge) {
        return productRepository.findByBadge(badge);
    }
}
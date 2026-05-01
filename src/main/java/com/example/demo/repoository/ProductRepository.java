package com.example.demo.repoository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Find all products by category (e.g. "Women", "Men")
    List<Product> findByCategory(String category);

    // Search products by name (e.g. search "shirt" → shows all shirts)
    List<Product> findByNameContaining(String name);
    
    // Find products by badge (e.g. "Sale", "New")
    List<Product> findByBadge(String badge);
}

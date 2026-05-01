package com.example.demo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.repoository.CartItemRepository;
import com.example.demo.repoository.ProductRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository repo;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addItem(CartItem item) {
        CartItem existing = repo.findByUserIdAndProductId(
                item.getUserId(), item.getProductId());

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
            repo.save(existing);
        } else {
            repo.save(item);
        }
    }

    @Override
    public void removeItem(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void updateQty(Long id, int quantity) {
        CartItem item = repo.findById(id).orElseThrow();
        item.setQuantity(quantity);
        repo.save(item);
    }

    @Override
    public List<CartItem> getCart(Long userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public void clearCart(Long userId) {
        List<CartItem> items = repo.findByUserId(userId);
        repo.deleteAll(items);
    }

    @Override
    public double getTotal(Long userId) {
        List<CartItem> items = getCart(userId);
        double total = 0;

        for (CartItem item : items) {
            // Get real product price from DB
            Product product = productRepository
                .findById(item.getProductId().intValue())
                .orElse(null);

            if (product != null) {
                total += product.getPrice() * item.getQuantity();
            }
        }
        return total;
    }

    @Override
    public CartItem getItemById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public int getCartCount(Long userId) {
        List<CartItem> items = repo.findByUserId(userId);
        int count = 0;
        for (CartItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }
}
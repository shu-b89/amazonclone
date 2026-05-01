package com.example.demo.services;

import java.util.List;
import com.example.demo.entity.CartItem;

public interface CartService {

    void addItem(CartItem item);

    void removeItem(Long id);

    void updateQty(Long id, int quantity);

    List<CartItem> getCart(Long userId);

    void clearCart(Long userId);

    double getTotal(Long userId);

	CartItem getItemById(Long id);
	
	int getCartCount(Long userId);

	   
}

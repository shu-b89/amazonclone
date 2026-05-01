package com.example.demo.repoository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	 List<CartItem> findByUserId(Long userId);
	 CartItem findByUserIdAndProductId(Long userId, Long productId);
	

}

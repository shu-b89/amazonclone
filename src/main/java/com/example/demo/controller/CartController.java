package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.services.CartService;
import com.example.demo.repoository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    // GET /cart
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Long userId = getSessionUserId(session);
        List<CartItem> items = cartService.getCart(userId);

        // Build a map of productId -> Product for the view
        Map<Long, Product> productMap = new HashMap<>();
        double total = 0;
        int totalQty = 0;

        for (CartItem item : items) {
            Product product = productRepository
                .findById(item.getProductId().intValue())
                .orElse(null);
            if (product != null) {
                productMap.put(item.getProductId(), product);
                total += product.getPrice() * item.getQuantity();
            }
            totalQty += item.getQuantity();
        }

        model.addAttribute("items", items);
        model.addAttribute("productMap", productMap);
        model.addAttribute("total", total);
        model.addAttribute("totalQty", totalQty);
        return "cart";
    }

    // POST /cart/add
    @PostMapping("/add")
    public String addItem(
            @RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {

        Long userId = getSessionUserId(session);
        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId((long) productId);
        item.setQuantity(quantity);
        cartService.addItem(item);
        return "redirect:/products/" + productId + "?added=true";
    }

    // POST /cart/remove
    @PostMapping("/remove")
    public String removeItem(@RequestParam Long id) {
        cartService.removeItem(id);
        return "redirect:/cart";
    }

    // POST /cart/update
    @PostMapping("/update")
    public String updateQty(
            @RequestParam Long id,
            @RequestParam String action) {

        CartItem item = cartService.getItemById(id);
        if (action.equals("increase")) {
            cartService.updateQty(id, item.getQuantity() + 1);
        } else {
            if (item.getQuantity() > 1) {
                cartService.updateQty(id, item.getQuantity() - 1);
            } else {
                cartService.removeItem(id);
            }
        }
        return "redirect:/cart";
    }

    // Helper
    private Long getSessionUserId(HttpSession session) {
        Object user = session.getAttribute("loggedInUser");
        if (user instanceof User) {
            return (long) ((User) user).getId();
        }
        return 1L;
    }

    @ModelAttribute
    public void addCartCount(HttpSession session, Model model) {
        Long userId = getSessionUserId(session);
        model.addAttribute("cartCount", cartService.getCartCount(userId));
    }
}
package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repoository.ProductRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    // GET /checkout — show checkout form
    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        Long userId = getUserId(session);
        List<CartItem> items = cartService.getCart(userId);

        if (items.isEmpty()) {
            return "redirect:/cart";
        }

        double total = 0;
        double mrpTotal = 0;
        for (CartItem item : items) {
            Product p = productRepository
                .findById(item.getProductId().intValue()).orElse(null);
            if (p != null) {
                total += p.getPrice() * item.getQuantity();
                mrpTotal += p.getMrp() * item.getQuantity();
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("mrpTotal", mrpTotal);
        return "checkout";
    }

    // POST /checkout/place — place the order
    @PostMapping("/checkout/place")
    public String placeOrder(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam String addressLine,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String pincode,
            @RequestParam String paymentMethod,
            HttpSession session) {

        Long userId = getUserId(session);
        List<CartItem> cartItems = cartService.getCart(userId);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Build order
        Order order = new Order();
        order.setUserId(userId);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setPhone(phone);
        order.setEmail(email);
        order.setAddressLine(addressLine);
        order.setCity(city);
        order.setState(state);
        order.setPincode(pincode);
        order.setPaymentMethod(paymentMethod);

        // Calculate totals
        double total = 0;
        double mrpTotal = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product p = productRepository
                .findById(cartItem.getProductId().intValue()).orElse(null);
            if (p != null) {
                total += p.getPrice() * cartItem.getQuantity();
                mrpTotal += p.getMrp() * cartItem.getQuantity();

                OrderItem oi = new OrderItem();
                oi.setProductId(cartItem.getProductId());
                oi.setProductName(p.getName());
                oi.setProductImage(p.getImageUrl());
                oi.setPrice(p.getPrice());
                oi.setQuantity(cartItem.getQuantity());
                orderItems.add(oi);
            }
        }

        order.setTotalAmount(total);
        order.setMrpTotal(mrpTotal);

        // Save order
        Order savedOrder = orderService.createOrder(order, orderItems);

        // Clear cart after order placed
        cartService.clearCart(userId);

        // Store order in session for success page
        session.setAttribute("lastOrder", savedOrder);

        return "redirect:/order/success";
    }

    // GET /order/success
    @GetMapping("/order/success")
    public String orderSuccess(HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("lastOrder");
        if (order == null) {
            return "redirect:/home";
        }
        model.addAttribute("order", order);
        return "order-success";
    }

    // GET /orders
    @GetMapping("/orders")
    public String myOrders(HttpSession session, Model model) {
        Long userId = getUserId(session);
        List<Order> orders = orderService.getOrdersByUser(userId);

        // Get items for each order
        java.util.Map<Long, List<OrderItem>> orderItemsMap = new java.util.HashMap<>();
        for (Order o : orders) {
            orderItemsMap.put(o.getId(), orderService.getOrderItems(o.getId()));
        }

        model.addAttribute("orders", orders);
        model.addAttribute("orderItemsMap", orderItemsMap);
        return "orders";
    }

    // Helper
    private Long getUserId(HttpSession session) {
        Object user = session.getAttribute("loggedInUser");
        if (user instanceof User) {
            return (long) ((User) user).getId();
        }
        return 1L;
    }
    @Autowired
    private CartService cartService1;

    @ModelAttribute
    public void addCartCount(
            HttpSession session,
            Model model) {
        Long userId = getUserId(session);
        model.addAttribute("cartCount", cartService.getCartCount(userId));
    }
}
package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.services.CartService;
import com.example.demo.services.ProductService;
import com.example.demo.repoository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // HOME PAGE
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("products", productService.findAll());
        return "home";
    }

    // PRODUCTS PAGE
    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String badge,
            Model model) {

        if (category != null && !category.isEmpty()) {
            model.addAttribute("products", productService.findByCategory(category));
            model.addAttribute("selectedCategory", category);
        } else if (badge != null && !badge.isEmpty()) {
            model.addAttribute("products", productService.findByBadge(badge));
            model.addAttribute("selectedBadge", badge);
        } else {
            model.addAttribute("products", productService.findAll());
        }
        return "products";
    }

    // PRODUCT DETAIL PAGE
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable int id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }
        // Related products — same category, excluding current
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts",
            productService.findByCategory(product.getCategory())
                .stream()
                .filter(p -> p.getId() != id)
                .limit(4)
                .toList()
        );
        return "product-detail";
    }

    // SEARCH
    @GetMapping("/search")
    public String search(@RequestParam String q, Model model) {
        model.addAttribute("products", productService.search(q));
        model.addAttribute("searchQuery", q);
        return "products";
    }

    // SHOW ADD PRODUCT FORM
    @GetMapping("/admin/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    // SAVE NEW PRODUCT
    @PostMapping("/admin/add-product")
    public String saveProduct(
            @RequestParam String name,
            @RequestParam String brand,
            @RequestParam double price,
            @RequestParam double mrp,
            @RequestParam String imageUrl,
            @RequestParam String category,
            @RequestParam double rating,
            @RequestParam int reviews,
            @RequestParam(required = false, defaultValue = "") String badge,
            @RequestParam(required = false, defaultValue = "false") boolean prime) {

        Product p = new Product();
        p.setName(name);
        p.setBrand(brand);
        p.setPrice(price);
        p.setMrp(mrp);
        p.setImageUrl(imageUrl);
        p.setCategory(category);
        p.setRating(rating);
        p.setReviews(reviews);
        p.setBadge(badge);
        p.setPrime(prime);

        productRepository.save(p);

        return "redirect:/admin/add-product?success=true";
    }
 // ADD TO WISHLIST
    @PostMapping("/wishlist/add")
    public String addToWishlist(
            @RequestParam int productId,
            jakarta.servlet.http.HttpSession session,
            jakarta.servlet.http.HttpServletRequest request) {

        // Get existing wishlist from session or create new one
        java.util.List<Integer> wishlist =
            (java.util.List<Integer>) session.getAttribute("wishlist");

        if (wishlist == null) {
            wishlist = new java.util.ArrayList<>();
        }

        // Add product ID if not already in wishlist
        if (!wishlist.contains(productId)) {
            wishlist.add(productId);
        }

        session.setAttribute("wishlist", wishlist);

        // Go back to the product detail page
        return "redirect:/products/" + productId + "?wishlisted=true";
    }

    // VIEW WISHLIST PAGE
    @GetMapping("/wishlist")
    public String viewWishlist(
            jakarta.servlet.http.HttpSession session,
            Model model) {

        java.util.List<Integer> wishlist =
            (java.util.List<Integer>) session.getAttribute("wishlist");

        if (wishlist == null || wishlist.isEmpty()) {
            model.addAttribute("wishlistProducts", new java.util.ArrayList<>());
        } else {
            // Fetch each product from DB by ID
            java.util.List<Product> wishlistProducts = wishlist.stream()
                .map(id -> productService.findById(id))
                .filter(p -> p != null)
                .toList();
            model.addAttribute("wishlistProducts", wishlistProducts);
        }

        return "wishlist";
    }
    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addCartCount(
            jakarta.servlet.http.HttpSession session,
            Model model) {
        Object user = session.getAttribute("loggedInUser");
        Long userId = 1L;
        if (user instanceof com.example.demo.entity.User) {
            userId = (long) ((com.example.demo.entity.User) user).getId();
        }
        model.addAttribute("cartCount", cartService.getCartCount(userId));
    }
}
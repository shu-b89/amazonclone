package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.entity.User;
import com.example.demo.services.UserServices;

@Controller
public class Mycontroller {

    @Autowired
    private UserServices userserv;

    // GET /login — show login page
    @GetMapping("/login")
    public String showLogin(Model model) {
        return "login";
    }

    // POST /login — handle login form
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        try {
            User user = userserv.login(email, password);
            session.setAttribute("loggedInUser", user);
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password. Please try again.");
            return "login";
        }
    }

    // GET /register — show register page
    @GetMapping("/register")
    public String showRegister(Model model) {
        return "register";
    }

    // POST /register — handle register form
    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            // Check if email already exists
            User existing = userserv.findByEmail(email);
            if (existing != null) {
                model.addAttribute("error", "Email already registered! Please login.");
                return "register";
            }

            // Create new user
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(phone);
            user.setRole("USER");

            userserv.saveUser(user);
            redirectAttributes.addFlashAttribute("success",
                "Registration successful! Please login.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }

    // GET /logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
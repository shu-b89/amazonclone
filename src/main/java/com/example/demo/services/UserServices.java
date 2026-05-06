package com.example.demo.services;

import com.example.demo.entity.User;

public interface UserServices {
    User login(String email, String password);
    User findByEmail(String email);
    User saveUser(User user);
}
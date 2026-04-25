package com.example.demo.services;

import com.example.demo.entity.User;
import com.example.demo.entity.UserDetails;

public interface UserServices {

    User register(UserDetails details);

    User login(String email, String password);

    User findByEmail(String email);
}
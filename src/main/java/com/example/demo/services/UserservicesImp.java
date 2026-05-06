package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repoository.UserRepository;
import com.example.demo.entity.User;

@Service
public class UserservicesImp implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
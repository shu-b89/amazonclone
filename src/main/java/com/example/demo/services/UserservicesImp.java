package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.example.demo.Details.UserDetails;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.repoository.UserRepository;

@Service
public class UserservicesImp implements UserServices {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public user register(UserDetails Details) {
		if (userRepository.existsByEmail(Details.getEmail())) {
			throw new DuplicateEmailException("Email already registered:" + Details.getEmail());
		}
		
		User user = new User();
		user.setName(Details.getName());
		user.setEmail(Details.getEmail());
		user.setPassword(Details.getPassword());
		user.setPhone(Details.getPhone());
		user.setRoles("User");
		
		return userRepository.save(user);
	}
	@Override
	 public User login(String email, String password){
		User user = userRepository.findByEmailAndPassword(email,password);
		if (user== null) {
			throw new InvalidCredentialsException("Invalid email or password");
		}
		return user;
	}
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
}

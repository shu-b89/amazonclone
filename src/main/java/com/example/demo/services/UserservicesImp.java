package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repoository.UserRepository;
import com.example.demo.controller.DuplicateEmailException;
import com.example.demo.controller.InvalidCredentialsException;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDetails;

@Service
public abstract class UserservicesImp implements UserServices {
	@Autowired
	private UserRepository userRepository; 
	
	@Override
	public User register(UserDetails Details) {
		if (userRepository.findByEmail(Details.getEmail()) != null) {
			throw new DuplicateEmailException("Email already registered:" + Details.getEmail());
		}
		
		User user = new User();
		user.setName(Details.getName());
		user.setEmail(Details.getEmail());
		user.setPassword(Details.getPassword());
		user.setPhone(Details.getPhone());
		user.setRole("User");
		
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

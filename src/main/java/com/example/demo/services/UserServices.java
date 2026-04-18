package com.example.demo.services;
import com.example.demo.Details.UserDetails;

public interface UserServices {

		User register(UserDetails dto);
		
		User login(String email, sreing password);
		
		user findByEmail(String email);

}

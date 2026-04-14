package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.enity.User;
import com.example.demo.repoository.UserRepository;
@Service
public class UserServiceImpl implements UserService
{

	@Autowired
	private UserRepository userRepos;
	@Override
	public boolean registerUser(User user) {
		try {
			userRepos.save(user);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

package com.example.demo.repoository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.enity.User;

public interface UserRepository extends JpaRepository<User, Integer>
{

}

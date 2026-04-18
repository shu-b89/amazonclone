package com.example.demo.repoository;
import com.example.demo.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.enity.User;

public interface UserRepository extends JpaRepository<User, Integer>
{
	User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByEmailAndPassword(String email, String password);

}

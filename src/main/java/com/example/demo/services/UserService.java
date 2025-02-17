package com.example.demo.services;

import javax.management.RuntimeErrorException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository) {
		// TODO Auto-generated constructor stub
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public User userRegister(User user) {
		if( userRepository.findByUsername(user.getUsername()).isPresent()) {
			 throw new RuntimeException("Username is already taken");
		}
		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			 throw new RuntimeException("Email is already taken");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepository.save(user);
		
		
	}
}

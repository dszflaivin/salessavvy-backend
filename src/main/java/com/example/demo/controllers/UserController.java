package com.example.demo.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/users")
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			User registeredUser = userService.userRegister(user);
			return ResponseEntity.ok(Map.of("message", "User registered successfully", "user", registeredUser));
		} catch(Exception e) {
			return ResponseEntity.badRequest().body(Map.of("ERROR",e.getMessage()));
		}
	}
	
	 @GetMapping("/profile")
	    public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
	        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
	        
	        if (authenticatedUser == null) {
	            return ResponseEntity.status(401).body("Unauthorized");
	        }

	        return ResponseEntity.ok(Map.of(
	                "id", authenticatedUser.getUserId(),
	                "name", authenticatedUser.getUsername(),  // Ensure name is being returned
	                "email", authenticatedUser.getEmail(),
	                "role", authenticatedUser.getRole()
	            ));
	    }
}
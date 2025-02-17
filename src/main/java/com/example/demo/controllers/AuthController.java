package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.LoginDTO;
import com.example.demo.entities.User;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {

	AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDTO usercred, HttpServletResponse response) {

		try {
			User user =authService.authenticate(usercred.getUsername(), usercred.getPassword());
			String token = authService.generateToken(user);
			
			//set it as cookie 
			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			cookie.setPath("/");
			cookie.setMaxAge(3600);
			cookie.setDomain("localhost");
			response.addCookie(cookie);
			
			response.setHeader("set-cookie",
					String.format("authToken=%s; HttpOnly; Path=/; Max-Age=3600; SameSite=None",token));

			Map<String, Object> responseBody = new HashMap<>();
			responseBody.put("message", "LoginSuccess");
			responseBody.put("role",user.getRole().name());
			responseBody.put("username", user.getUsername());
		
			return ResponseEntity.ok(responseBody);
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error",e.getMessage()));
		}
	}
}

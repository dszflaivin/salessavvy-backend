package com.example.demo.controllers;

import java.util.HashMap;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.LoginDTO;
import com.example.demo.entities.User;
import com.example.demo.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {

	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDTO usercred, HttpServletResponse response) {

		try {
			User user = authService.authenticate(usercred.getUsername(), usercred.getPassword());
			String token = authService.generateToken(user);
			
			//set it as cookie 
			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			cookie.setPath("/");
			cookie.setMaxAge(3600);
//			cookie.setDomain("salessavvy-backend-z6no.onrender.com");

			response.addCookie(cookie);
			
			response.addHeader("Set-Cookie",
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

	@PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,HttpServletResponse response) {
		try {
        	User user=(User) request.getAttribute("authenticatedUser");
        	if (user == null) {
    			throw new RuntimeException("User not authenticated");
    		}
        	authService.logout(user);
            
            Cookie cookie = new Cookie("authToken", null);
            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
//            response.addHeader("Set-Cookie", "authToken=; HttpOnly; Path=/; Max-Age=0; SameSite=None; Secure");

            
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Logout successful");
            System.out.println("Logout request received!"); // Check if this prints in backend logs

            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Logout failed");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}

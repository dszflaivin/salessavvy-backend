package com.example.demo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CartItemService;

import jakarta.servlet.http.HttpServletRequest;

//@CrossOrigin(origins="http://localhost:5173", allowCredentials="true")
@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartItemService cartItemService;
	

	@GetMapping("/items/count")
	public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {
		User user =  userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
		
		int	count = cartItemService.getCartItemCount(user.getUserId());	
		
		return ResponseEntity.ok(count);
	}

	
	@GetMapping("/items")
	public ResponseEntity<Map<String, Object>> getCartItems(HttpServletRequest request) {
		
		User user = (User) request.getAttribute("authenticatedUser");
		Map<String, Object> response = cartItemService.getCartItems(user.getUserId());
		return ResponseEntity.ok(response);
	}

	
	@PostMapping("/add")
	public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		
		int quantity = request.containsKey("quantity") ? (int) request.get("quantity") : 1;
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

		cartItemService.addToCart(user.getUserId(), productId, quantity);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	
	@PutMapping("/update")
	public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		int quantity = (int) request.get("quantity");
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found with username:" + username));

				
		cartItemService.updateCartItemQuantity(user.getUserId(), productId, quantity);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String, Object> request) {
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found with username:" + username));
		
		cartItemService.deleteCartItem(user.getUserId(), productId);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}

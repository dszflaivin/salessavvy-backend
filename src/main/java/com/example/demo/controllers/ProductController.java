package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {
	
	@PostMapping("/getProducts")
	public ResponseEntity<?> getProducts() {
		
		return ResponseEntity.ok("All Products");
	}
}

package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin
public class PaymentController {
	
	@PostMapping("/doPayment")
	public ResponseEntity<?> payment() {
		
		return ResponseEntity.ok("Payment success");
	}
}

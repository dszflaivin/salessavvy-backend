package com.example.demo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SalesSavvyApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
	    System.setProperty("DB_URL", dotenv.get("DB_URL"));
	    System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
	    System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
	    System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
	    System.setProperty("RAZORPAY_KEY_ID", dotenv.get("RAZORPAY_KEY_ID"));
	    System.setProperty("RAZORPAY_KEY_SECRET", dotenv.get("RAZORPAY_KEY_SECRET"));
	    System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));

		SpringApplication.run(SalesSavvyApplication.class, args);
	}
}

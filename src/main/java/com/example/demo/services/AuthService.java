package com.example.demo.services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.JWTToken;
import com.example.demo.entities.User;
import com.example.demo.repositories.JWTTokenRepository;
import com.example.demo.repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

	private final Key SIGNING_KEY;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JWTTokenRepository jwtTokenRepository;


	public AuthService(UserRepository userRepository, JWTTokenRepository jwtTokenRepository, @Value("${jwt.secret}") String jwtSecret) {
		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		if (jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) { 
			throw new IllegalArgumentException("JWT_SECRET in application.properties must be at least 64 bytes long for HS512.");
		}
		this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));		

	}

	public User authenticate(String username, String password) {
		User user = userRepository.findByUsername(username) .orElseThrow(() -> new RuntimeException("Invalid username or password"));
		if  (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid username or password");
		}
		return user;
	}

	public String generateToken(User user) {
		String token;

		LocalDateTime currentTime = LocalDateTime.now();

		// fetching existing token from repo
		JWTToken existingToken = jwtTokenRepository.findByUserId(user.getUserId());

		// get time of token expiry


		if(existingToken!=null && currentTime.isBefore(existingToken.getExpiresAt())) {
			token = existingToken.getToken();
		} else {
			token = generateNewToken(user);
			if(existingToken!=null) {
				jwtTokenRepository.delete(existingToken);
			}
			saveToken(token, user);
		}
		return token;
	}

	public String generateNewToken(User user) {
		String token = Jwts.builder()
				.setSubject(user.getUsername())
				.claim("role", user.getRole().name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+3600000))
				.signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
				.compact();

		return token;
	}


	public void saveToken(String token,User user) {		
		JWTToken jwtToken = new JWTToken(user, token, LocalDateTime.now().plusHours(1));
		jwtTokenRepository.save(jwtToken);
	}

	public boolean validateToken(String token) {
		try {
			System.err.println("VALIDATING TOKEN...");

			Jwts.parserBuilder()
			.setSigningKey(SIGNING_KEY)
			.build()
			.parseClaimsJws(token);

			Optional<JWTToken> jwtToken = jwtTokenRepository.findByToken(token);
			if (jwtToken.isPresent()) {
				System.err.println("Token Expiry: " + jwtToken.get().getExpiresAt());
				System.err.println("Current Time: " + LocalDateTime.now());
				return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
			}

			return false;
		} catch (Exception e) {
			System.err.println("Token validation failed: " + e.getMessage());
			return false;
		}

	}

	public String extractUsername(String token) { 
		return Jwts.parserBuilder() 
				.setSigningKey(SIGNING_KEY) 
				.build() 
				.parseClaimsJws(token) 
				.getBody() 
				.getSubject(); 
	}

	public void logout(User user) { 
		jwtTokenRepository.deleteByUserId(user.getUserId());
	}
}

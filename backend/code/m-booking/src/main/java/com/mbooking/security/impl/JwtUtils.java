package com.mbooking.security.impl;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	private static final String APP_NAME = "connectedapp";
	
	// Ovo ce za svako novo pokretanje da generise novi kljuc
	// private static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
	
	// Ovako jedan isti token za jednog korisnika koristimo tokom celog razvoja
	private static final Key SECRET = Keys.hmacShaKeyFor("secretsecretsecretsecretsecretsecret".getBytes());
	
	//private static final int EXPIRES_IN = 3600; // <- aktiviracemo kada zavrsimo projekat
	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	
	public String generateToken(String username) {
		return Jwts.builder()
				.setIssuer(APP_NAME)
				.setSubject(username)
				//.setIssuedAt(new Date())
				//.setExpiration(new Date(new Date().getTime() + EXPIRES_IN * 1000))
				.signWith(SECRET, SIGNATURE_ALGORITHM)
				.compact();
	}
	
	public String getToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}
	
	public String getUsername(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
	
	public boolean notExpired(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token)
				.getBody();
		return new Date().before(claims.getExpiration());
	}
	
}
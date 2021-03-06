package com.ccc.api.controller;

import java.util.Calendar;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;

import com.ccc.api.model.Users;


//@Slf4j
@Component
public class JwtUtils {

  private String issuer = "ccc";

  private SecretKey secretKey;

  public JwtUtils(@Value("${jwt.secret}") String secret) {
	  System.out.println(secret);
    byte[] keyBytes = Decoders.BASE64URL.decode(secret);
    secretKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String toToken(Users user) {
    Calendar expiration = Calendar.getInstance();
    expiration.add(Calendar.DATE, 7);
    
    
    return Jwts.builder().setIssuer(issuer).setSubject(user.getUserId().toString())
        .setExpiration(expiration.getTime()).setClaims(user.toClaims()).signWith(secretKey).compact();
  }

  public Users toUser(String token) {
    try {
    	Jws<Claims> jws =	Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return Users.fromClaims(jws.getBody());
    } catch (JwtException e) {
//      log.warn("JWT Token Error", e);
    	System.out.println(e);
      return null;
    }
  }
}
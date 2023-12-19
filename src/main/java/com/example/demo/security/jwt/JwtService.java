package com.example.demo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final static Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .signWith(getSecretKey())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .compact();
    }

    public boolean isTokenValid(String token) {
        if (token == null) {
            logger.error("Given token is null. Token not valid.");
            return false;
        }

        if (extractExpiration(token).after(new Date())) {
            logger.error("Token is expired. Token not valid.");
        }

        logger.info("Token is valid.");
        return true;
    }

    public String extractJwt(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        } else {
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            logger.error("Can not extract all claims from given token.");
        }
        return claims;
    }

    private Key getSecretKey() {
        byte[] bytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

}

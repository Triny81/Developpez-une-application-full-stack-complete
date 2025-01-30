package com.openclassrooms.mddapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JWTService {
    private static final String SECRET = "MY_SECRET_KEY_123456789012345678901234567890";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    
        return token;
    }

    public Claims extractClaims(String token) {
        try {
    
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // ✅ Vérifier que la clé secrète est correcte
                    .build()
                    .parseClaimsJws(token) // 🔥 C'est ici que l'erreur "headers cannot be empty" se produit
                    .getBody();
    
            return claims;
        } catch (Exception e) {
            throw e;
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token))
                && extractClaims(token).getExpiration().after(new Date());
    }

    public Jwt decodeToken(String token) {
        Claims claims = extractClaims(token);
        return Jwt.withTokenValue(token)
                .header("alg", "HS256")
                .subject(claims.getSubject())
                .claim("roles", claims.get("roles"))
                .issuedAt(claims.getIssuedAt().toInstant())
                .expiresAt(claims.getExpiration().toInstant())
                .build();
    }
}

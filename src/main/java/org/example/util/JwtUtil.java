package org.example.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationMs;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getEmail())
        .claim("role", user.getRole())
        .claim("userId", user.getId())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException ex) {
      log.error("Token expired", ex);
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported token", ex);
    } catch (MalformedJwtException ex) {
      log.error("Malformed token", ex);
    } catch (SignatureException ex) {
      log.error("Invalid signature", ex);
    } catch (IllegalArgumentException ex) {
      log.error("Empty token", ex);
    }
    return false;
  }

  public String extractEmail(String token) {
    return extractClaims(token).getSubject();
  }

  public String extractRole(String token) {
    return extractClaims(token).get("role", String.class);
  }

  public Long extractUserId(String token) {
    return extractClaims(token).get("userId", Long.class);
  }

  public Claims extractClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

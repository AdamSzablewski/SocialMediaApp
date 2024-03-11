package com.adamszablewski.SocialMediaApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JwtUtil {
    @Value("${jwt.value}")
    private String JWT_SECRET;

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT has expired");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("JWT is malformed or has an invalid signature");
        } catch (Exception e) {
            throw new RuntimeException("JWT validation failed: " + e.getMessage());
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getUserIdFromToken(String token) {
        StringBuilder sb = new StringBuilder(token);
        if (token.startsWith("Bearer ")){
            sb.delete(0,7);
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build().parseClaimsJws(sb.toString())
                .getBody();
        return claims.get("userId", Long.class);
    }
}

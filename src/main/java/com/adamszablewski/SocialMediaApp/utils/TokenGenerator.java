package com.adamszablewski.SocialMediaApp.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static com.adamszablewski.SocialMediaApp.utils.JwtUtil.JWT_SECRET;


@Component
public class TokenGenerator {

    public String  generateToken(Long userId){

        Date currentDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

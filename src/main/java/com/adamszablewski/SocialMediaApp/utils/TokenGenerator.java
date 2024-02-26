package com.adamszablewski.SocialMediaApp.utils;

import com.adamszablewski.SocialMediaApp.enteties.JWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;



@Component
@AllArgsConstructor
public class TokenGenerator {

    private final JwtUtil jwtUtil;

    public JWT  generateToken(Long userId){
        Date currentDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

        String tokenValue = Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        return new JWT(tokenValue, userId);
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtUtil.getJWT_SECRET());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

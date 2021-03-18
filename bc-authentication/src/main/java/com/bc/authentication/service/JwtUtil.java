package com.bc.authentication.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    public String createToken(String username, String secret, Duration expireDuration) {
        Date expirationDate = new Date(System.currentTimeMillis() + expireDuration.toMillis());

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return "Bearer " + Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}

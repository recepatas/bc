package com.bc.productlisting;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

public class TestUtil {

    public static String createToken(String username, String secret) {
        Date expirationDate = new Date(System.currentTimeMillis() + Duration.ofHours(1).toMillis());

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return "Bearer " + Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}

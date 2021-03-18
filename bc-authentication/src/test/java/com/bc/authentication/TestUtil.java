package com.bc.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

public class TestUtil {

    public static Claims parseToken(String token, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.split(" ")[1])
                .getBody();
    }

    public static boolean checkExpireDate(Date date, Duration duration) {
        long diff = date.getTime() - System.currentTimeMillis();
        return diff <= duration.toMillis() && diff > duration.minus(Duration.parse("PT5s")).toMillis();
    }

}

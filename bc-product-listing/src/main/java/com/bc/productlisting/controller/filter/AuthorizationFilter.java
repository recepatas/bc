package com.bc.productlisting.controller.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationFilter extends GenericFilterBean {

    @Value("${bc.authentication.jwt.secret}")
    private String secret;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String usernameRequestFor = "";
        try {
            usernameRequestFor = req.getRequestURI().split("/")[2];
        } catch (Exception e) {}

        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = validateToken(token);

        if (!usernameRequestFor.equals(username)) {
            logger.info("User is not authenticated with token: " + token + ", usernameRequestFor:" + usernameRequestFor);

            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.getWriter().write("User is not authenticated");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String validateToken(String token) {
        String username = null;

        if (token != null && token.startsWith("Bearer")) {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            try {
                username = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token.split(" ")[1])
                        .getBody()
                        .getSubject();
            } catch (Exception e) {
                logger.info("Auth error" + e.getMessage());
            }
        }

        return username;
    }
}
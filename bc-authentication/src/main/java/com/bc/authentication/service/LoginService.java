package com.bc.authentication.service;

import com.bc.authentication.repository.UserRepository;
import com.bc.authentication.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${bc.authentication.jwt.expireDuration.default}")
    private Duration tokenExpireDuration;

    @Value("${bc.authentication.jwt.expireDuration.rememberMe}")
    private Duration tokenExpireDurationRememberMe;

    @Value("${bc.authentication.jwt.secret}")
    private String secret;

    public void saveUser(String username, String password) {
        User user = new User();
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAddress("address");
        user.setName("name");
        user.setOwnerName("owner name");
        user.setPhoneNumber("1321");
        userRepository.save(user);
    }

    public String login(String username, String password, Boolean rememberMe) {
        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        Duration expireDuration = rememberMe ? tokenExpireDurationRememberMe : tokenExpireDuration;

        return jwtUtil.createToken(username, secret, expireDuration);
    }

}

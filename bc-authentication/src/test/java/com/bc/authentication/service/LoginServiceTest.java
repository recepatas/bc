package com.bc.authentication.service;

import com.bc.authentication.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    private LoginService loginService;

    private final String secret = "value";
    private final Duration tokenExpireDuration = Duration.ofHours(12);
    private final Duration tokenExpireDurationRememberMe = Duration.ofDays(7);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(loginService, "secret", secret);
        ReflectionTestUtils.setField(loginService, "tokenExpireDuration", tokenExpireDuration);
        ReflectionTestUtils.setField(loginService, "tokenExpireDurationRememberMe", tokenExpireDurationRememberMe);
    }

    @Test
    public void should_return_token_for_login() {
        String expectedToken = "Bearer token";
        String username = "username1";
        String pass = "password1";

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pass)))
                .thenReturn(authentication);
        when(jwtUtil.createToken(username, secret, tokenExpireDuration)).thenReturn(expectedToken);

        Assert.assertEquals(expectedToken, loginService.login(username, pass, false));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(1)).createToken(any(), any(), any());
    }

    @Test
    public void should_set_expiration_date_for_remember_me_login() {
        String expectedToken = "Bearer token";
        String username = "username1";
        String pass = "password1";

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pass)))
                .thenReturn(authentication);
        when(jwtUtil.createToken(username, secret, tokenExpireDurationRememberMe)).thenReturn(expectedToken);

        Assert.assertEquals(expectedToken, loginService.login(username, pass, true));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(1)).createToken(any(), any(), any());
    }


}
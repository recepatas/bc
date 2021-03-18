package com.bc.authentication.controller;

import com.bc.authentication.dto.LoginRequest;
import com.bc.authentication.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @MockBean
    private LoginService loginService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_token_when_user_is_logged_in() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username1", "password1", false);
        String expectedToken = "Bearer token";

        when(loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getRememberMe()))
                .thenReturn(expectedToken);

        mockMvc.perform(
                post("/auth/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));
    }

    @Test
    public void should_return_error_when_authentication_fails() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username1", "password1", false);

        when(loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getRememberMe()))
                .thenThrow(new BadCredentialsException(""));

        mockMvc.perform(
                post("/auth/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized());
    }

}

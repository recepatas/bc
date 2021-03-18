package com.bc.authentication.controller;

import com.bc.authentication.dto.ApiResponse;
import com.bc.authentication.dto.LoginRequest;
import com.bc.authentication.dto.LoginResponse;
import com.bc.authentication.dto.SignupRequest;
import com.bc.authentication.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    LoginService loginService;

    @GetMapping("/auth-restricted")
    public ResponseEntity<ApiResponse> status() {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Auth service is running"), HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getRememberMe());

        logger.info("User logged in: " + loginRequest.getUsername());
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequest signupRequest) {
        loginService.saveUser(signupRequest.getUsername(), signupRequest.getPassword());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "User created"), HttpStatus.OK);
    }

}

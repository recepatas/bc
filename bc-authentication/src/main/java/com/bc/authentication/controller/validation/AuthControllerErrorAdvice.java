package com.bc.authentication.controller.validation;

import com.bc.authentication.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class AuthControllerErrorAdvice {

    private Logger logger = LoggerFactory.getLogger(AuthControllerErrorAdvice.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> authenticationException(AuthenticationException ex) {
        logger.info("Authentication failed: " + ex.getMessage());
        ApiResponse errorResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }

        ApiResponse errorResponse = new ApiResponse(HttpStatus.BAD_REQUEST, String.join("\n", errors));
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}

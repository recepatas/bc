package com.bc.authentication.dto;

import com.bc.authentication.controller.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LoginRequest implements Serializable {

    @NotNull(message = "Username can not be empty")
    private String username;

    @ValidPassword
    private String password;

    private Boolean rememberMe = false;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password, Boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}


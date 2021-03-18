package com.bc.authentication.dto;

import com.bc.authentication.controller.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SignupRequest implements Serializable {

    @NotNull(message = "Username can not be empty")
    private String username;

    @ValidPassword
    private String password;

    public SignupRequest() {
    }

    public SignupRequest(String username, String password) {
        this.username = username;
        this.password = password;
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

}


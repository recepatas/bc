package com.bc.authentication.controller.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Value("${bc.authentication.minPasswordLength}")
    private int minPasswordLength;

    private final String alphaNumRegex = "^[A-Za-z0-9]{" + minPasswordLength + ",}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password.length() >= minPasswordLength && password.matches(alphaNumRegex);
    }

}

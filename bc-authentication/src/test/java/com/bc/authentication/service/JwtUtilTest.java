package com.bc.authentication.service;

import com.bc.authentication.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;

@RunWith(MockitoJUnitRunner.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_return_token() {
        String username = "username1";
        String secret = "secret123456789012345678901234567890";

        String token = jwtUtil.createToken(username, secret, Duration.ofDays(12));

        Assert.assertEquals(username, TestUtil.parseToken(token, secret).getSubject());
    }

}
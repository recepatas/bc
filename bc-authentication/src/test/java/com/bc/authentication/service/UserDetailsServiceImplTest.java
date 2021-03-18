package com.bc.authentication.service;

import com.bc.authentication.repository.UserRepository;
import com.bc.authentication.repository.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_load_user_by_username() {
        String username = "username1";

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(new User(username, "123456")));

        Assert.assertEquals("123456", userDetailsService.loadUserByUsername(username).getPassword());
    }

}
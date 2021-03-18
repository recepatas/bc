package com.bc.authentication.controller;

import com.bc.authentication.TestUtil;
import com.bc.authentication.dto.LoginRequest;
import com.bc.authentication.dto.LoginResponse;
import com.bc.authentication.dto.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;
import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@ContextConfiguration(initializers = {AuthControllerIntegrationTest.Initializer.class})
public class AuthControllerIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${bc.authentication.jwt.secret}")
    private String secret;

    @Value("${bc.authentication.jwt.expireDuration.default}")
    private Duration tokenExpireDuration;

    @Value("${bc.authentication.jwt.expireDuration.rememberMe}")
    private Duration tokenExpireDurationRememberMe;

    ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void should_login_with_username_and_password() throws Exception {
        SignupRequest signupRequest = new SignupRequest("username1", "password1");
        LoginRequest loginRequest = new LoginRequest("username1", "password1", false);

        mockMvc.perform(
                post("/auth/sign-up")
                        .content(mapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        LoginResponse response = mapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);

        Date expectedTokenExpired = TestUtil.parseToken(response.getToken(), secret).getExpiration();

        assertTrue(TestUtil.checkExpireDate(expectedTokenExpired, tokenExpireDuration));
    }

    @Test
    public void should_return_token_when_login_with_remember_me_and_username_and_password() throws Exception {
        SignupRequest signupRequest = new SignupRequest("username2", "password1");
        LoginRequest loginRequest = new LoginRequest("username2", "password1", true);

        mockMvc.perform(
                post("/auth/sign-up")
                        .content(mapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        LoginResponse response = mapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);

        Date expectedTokenExpired = TestUtil.parseToken(response.getToken(), secret).getExpiration();

        assertTrue(TestUtil.checkExpireDate(expectedTokenExpired, tokenExpireDurationRememberMe));
    }

    @Test
    public void should_return_error_when_username_does_not_exists() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username3", "password1", false);

        mockMvc.perform(
                post("/auth/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("User name and password is not valid"));
    }

    @Test
    public void should_return_error_when_password_is_not_long_enough() throws Exception {
        SignupRequest signupRequest = new SignupRequest("username4", "password1");
        LoginRequest loginRequest = new LoginRequest("username4", "pass", true);

        mockMvc.perform(
                post("/auth/sign-up")
                        .content(mapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/auth/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Password"));
    }

    @Test
    public void should_return_error_when_password_is_not_alphanumeric() throws Exception {
        SignupRequest signupRequest = new SignupRequest("username5", "password1");
        LoginRequest loginRequest = new LoginRequest("username5", "password1_______", true);

        mockMvc.perform(
                post("/auth/sign-up")
                        .content(mapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/auth/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Password"));
    }

}

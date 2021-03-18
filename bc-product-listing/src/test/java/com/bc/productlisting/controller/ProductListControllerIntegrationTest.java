package com.bc.productlisting.controller;

import com.bc.productlisting.TestUtil;
import com.bc.productlisting.controller.filter.AuthorizationFilter;
import com.bc.productlisting.dto.ProductDto;
import com.bc.productlisting.dto.ProductListResponse;
import com.bc.productlisting.repository.entity.PaymentOption;
import com.bc.productlisting.repository.entity.ProductCategory;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ContextConfiguration(initializers = {ProductListControllerIntegrationTest.Initializer.class})
public class ProductListControllerIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    AuthorizationFilter authorizationFilter;

    @Value("${bc.authentication.jwt.secret}")
    private String secret;

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(authorizationFilter)
                .build();
    }

    @Test
    @Transactional
    public void should_return_list_of_products_by_pagination() throws Exception {
        ProductDto productDto1 = new ProductDto("product-1", 100, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("100.00"), PaymentOption.DIRECT,
                "delivery"
        );

        ProductDto productDto2 = new ProductDto("product-2", 1000, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("1000.00"), PaymentOption.DIRECT,
                "delivery"
        );

        ProductDto productDto3 = new ProductDto("product-3", 50, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("500.00"), PaymentOption.DIRECT,
                "delivery"
        );

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                get("/merchant/merchant-1/products?page=0&size=2")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk())
                .andReturn();

        ProductListResponse response = mapper.readValue(result.getResponse().getContentAsString(), ProductListResponse.class);

        assertEquals(3, response.getTotalProducts());
        assertEquals(2, response.getTotalPages());
        assertEquals(Arrays.asList(productDto1, productDto2), response.getProducts());
    }

    @Test
    @Transactional
    public void should_return_list_of_products_with_sorting_by_unit_price() throws Exception {
        ProductDto productDto1 = new ProductDto("product-1", 100, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("100.00"), PaymentOption.DIRECT,
                "delivery"
        );

        ProductDto productDto2 = new ProductDto("product-2", 1000, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("1000.00"), PaymentOption.DIRECT,
                "delivery"
        );

        ProductDto productDto3 = new ProductDto("product-3", 50, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("500.00"), PaymentOption.DIRECT,
                "delivery"
        );

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                get("/merchant/merchant-1/products?page=0&size=3&sort=unitPrice,asc")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk())
                .andReturn();

        ProductListResponse response = mapper.readValue(result.getResponse().getContentAsString(), ProductListResponse.class);

        assertEquals(3, response.getTotalProducts());
        assertEquals(1, response.getTotalPages());
        assertEquals(Arrays.asList(productDto1, productDto3, productDto2), response.getProducts());
    }

    @Test
    @Transactional
    public void should_return_list_of_products_with_sorting_by_inventory() throws Exception {
        ProductDto productDto1 = new ProductDto("product-1", 100, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("100.00"), PaymentOption.DIRECT,
                "delivery"
        );

        ProductDto productDto2 = new ProductDto("product-2", 1000, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("1000.00"), PaymentOption.DIRECT,
                "delivery"
        );

        ProductDto productDto3 = new ProductDto("product-3", 50, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("500.00"), PaymentOption.DIRECT,
                "delivery"
        );

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                get("/merchant/merchant-1/products?page=0&size=3&sort=inventory,desc")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk())
                .andReturn();

        ProductListResponse response = mapper.readValue(result.getResponse().getContentAsString(), ProductListResponse.class);

        assertEquals(3, response.getTotalProducts());
        assertEquals(1, response.getTotalPages());
        assertEquals(Arrays.asList(productDto2, productDto1, productDto3), response.getProducts());
    }

    @Test
    @Transactional
    public void should_return_list_of_products_with_inventory_more_than_limit() throws Exception {
        ProductDto productDto1 = new ProductDto("product-1", 100, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("100.00"), PaymentOption.DIRECT,
                "delivery"
        );

        ProductDto productDto2 = new ProductDto("product-2", 3, "merchant-1",
                ProductCategory.ELECTRONICS, "desc", new BigDecimal("1000.00"), PaymentOption.DIRECT,
                "delivery"
        );


        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/merchant/merchant-1/products")
                        .content(mapper.writeValueAsString(productDto2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                get("/merchant/merchant-1/products?page=0&size=3")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isOk())
                .andReturn();

        ProductListResponse response = mapper.readValue(result.getResponse().getContentAsString(), ProductListResponse.class);

        assertEquals(1, response.getTotalProducts());
        assertEquals(1, response.getTotalPages());
        assertEquals(Collections.singletonList(productDto1), response.getProducts());
    }

    @Test
    public void should_return_error_when_user_is_not_authenticated() throws Exception {
        mockMvc.perform(
                get("/merchant/merchant-1/products?page=0&size=2")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_return_error_when_request_is_for_another_user() throws Exception {
        mockMvc.perform(
                get("/merchant/merchant-2/products?page=0&size=2")
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("merchant-1", secret))
        )
                .andExpect(status().isUnauthorized());
    }

}

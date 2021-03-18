package com.bc.productlisting.controller;

import com.bc.productlisting.TestUtil;
import com.bc.productlisting.dto.ProductListResponse;
import com.bc.productlisting.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductListController.class)
public class ProductListControllerTest {

    @MockBean
    private ProductService productService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Value("${bc.authentication.jwt.secret}")
    private String secret;

    @Test
    public void should_return_token_when_user_is_logged_in() throws Exception {
        ProductListResponse productListResponse = new ProductListResponse();
        productListResponse.setTotalProducts(1);

        when(productService.getProducts("1", PageRequest.of(0, 10))).thenReturn(productListResponse);

        mockMvc.perform(
                get("/merchant/1/products?page=0&size=10")
                .header(HttpHeaders.AUTHORIZATION, TestUtil.createToken("1", secret))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProducts").value(1));
    }

}

package com.bc.productlisting.service;

import com.bc.productlisting.dto.ProductListResponse;
import com.bc.productlisting.repository.ProductRepository;
import com.bc.productlisting.repository.entity.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private final int minInventoryToList = 5;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(productService, "minInventoryToList", minInventoryToList);
    }

    @Test
    public void should_return_product_list_response() {
        String merchantId = "merchant";
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> productList = Collections.emptyList();

        when(productRepository.findByMerchantIdAndInventoryGreaterThan(merchantId, minInventoryToList, pageable))
                .thenReturn(new PageImpl<Product>(productList, pageable, 10));

        ProductListResponse productListResponse = productService.getProducts("merchant", pageable);

        Assert.assertEquals(10, productListResponse.getTotalProducts());
        Assert.assertEquals(2, productListResponse.getTotalPages());
    }

}
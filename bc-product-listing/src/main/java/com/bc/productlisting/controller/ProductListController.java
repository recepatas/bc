package com.bc.productlisting.controller;

import com.bc.productlisting.dto.ApiResponse;
import com.bc.productlisting.dto.ProductDto;
import com.bc.productlisting.dto.ProductListResponse;
import com.bc.productlisting.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class ProductListController {

    private Logger logger = LoggerFactory.getLogger(ProductListController.class);

    @Autowired
    ProductService productService;

    @GetMapping("/merchant/{merchantId}/products")
    public ResponseEntity<ProductListResponse> getProducts(@PathVariable String merchantId, Pageable pageable) {
        ProductListResponse products = productService.getProducts(merchantId, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/merchant/{merchantId}/products")
    public ResponseEntity<ApiResponse> addProduct(@PathVariable String merchantId,
                                                  @Valid @RequestBody ProductDto productDto) {
        productService.saveProduct(productDto);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Product is saved"), HttpStatus.OK);
    }

}

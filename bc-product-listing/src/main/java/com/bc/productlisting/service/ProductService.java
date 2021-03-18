package com.bc.productlisting.service;

import com.bc.productlisting.dto.ProductDto;
import com.bc.productlisting.dto.ProductListResponse;
import com.bc.productlisting.repository.ProductRepository;
import com.bc.productlisting.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${bc.product-listing.minInventoryToList}")
    private int minInventoryToList;

    public void saveProduct(ProductDto productDto) {
        Product product = new Product();
        product.setCategory(productDto.getCategory());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setDeliveryOption(productDto.getDeliveryOption());
        product.setInventory(productDto.getInventory());
        product.setMerchantId(productDto.getMerchantId());
        product.setPaymentOption(productDto.getPaymentOption());
        product.setUnitPrice(productDto.getUnitPrice());
        productRepository.save(product);
    }

    public ProductListResponse getProducts(String merchantId, Pageable pageable) {
        ProductListResponse productListResponse = new ProductListResponse();

        Page<Product> page = productRepository.findByMerchantIdAndInventoryGreaterThan(merchantId, minInventoryToList, pageable);

        List<ProductDto> products = page.map(product -> new ProductDto(product.getName(), product.getInventory(), product.getMerchantId(),
                product.getCategory(), product.getDescription(), product.getUnitPrice(),
                product.getPaymentOption(), product.getDeliveryOption())).toList();

        productListResponse.setTotalProducts(page.getTotalElements());
        productListResponse.setTotalPages(page.getTotalPages());
        productListResponse.setProducts(products);

        return productListResponse;
    }
}

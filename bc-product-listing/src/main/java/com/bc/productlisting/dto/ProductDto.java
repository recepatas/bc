package com.bc.productlisting.dto;

import com.bc.productlisting.repository.entity.PaymentOption;
import com.bc.productlisting.repository.entity.ProductCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductDto implements Serializable {

    @NotNull(message = "product name can not be empty")
    private String name;

    @PositiveOrZero
    private Integer inventory;

    private String merchantId;

    private ProductCategory category;

    private String description;

    private BigDecimal unitPrice;

    private PaymentOption paymentOption;

    private String deliveryOption;

    public ProductDto() {
    }

    public ProductDto(String name, Integer inventory, String merchantId, ProductCategory category, String description,
                      BigDecimal unitPrice, PaymentOption paymentOption, String deliveryOption) {
        this.name = name;
        this.inventory = inventory;
        this.merchantId = merchantId;
        this.category = category;
        this.description = description;
        this.unitPrice = unitPrice;
        this.paymentOption = paymentOption;
        this.deliveryOption = deliveryOption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public PaymentOption getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(PaymentOption paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(name, that.name) && Objects.equals(inventory, that.inventory) && Objects.equals(merchantId, that.merchantId) && category == that.category && Objects.equals(description, that.description) && Objects.equals(unitPrice, that.unitPrice) && paymentOption == that.paymentOption && Objects.equals(deliveryOption, that.deliveryOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inventory, merchantId, category, description, unitPrice, paymentOption, deliveryOption);
    }
}


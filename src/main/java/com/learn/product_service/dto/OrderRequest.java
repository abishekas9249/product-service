package com.learn.product_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull
    @Min(value = 1,message = "Quantity must be at least 1")
    @Max(value = 50,message = "Max 50 per order")
    private Integer quantity;
}

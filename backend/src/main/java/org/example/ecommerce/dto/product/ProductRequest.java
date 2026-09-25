package org.example.ecommerce.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    private String imageUrl;

    @NotNull
    private Long categoryId;
}
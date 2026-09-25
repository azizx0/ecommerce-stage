package org.example.ecommerce.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty(message = "La commande doit contenir au moins un article")
    @Valid
    private List<OrderItemRequest> items;

    @NotBlank
    private String shippingAddress;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
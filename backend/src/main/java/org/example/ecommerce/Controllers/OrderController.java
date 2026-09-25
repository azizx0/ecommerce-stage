package org.example.ecommerce.Controllers;

import org.example.ecommerce.dto.order.OrderRequest;
import org.example.ecommerce.dto.order.OrderResponse;
import org.example.ecommerce.entity.OrderStatus;
import org.example.ecommerce.Services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request,
                                                Authentication authentication) {
        return ResponseEntity.ok(orderService.create(request, authentication));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getMyOrders(authentication));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
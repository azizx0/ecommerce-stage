package org.example.ecommerce.Services;

import org.example.ecommerce.dto.order.OrderRequest;
import org.example.ecommerce.dto.order.OrderResponse;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.exception.BadRequestException;
import org.example.ecommerce.exception.ResourceNotFoundException;
import org.example.ecommerce.Repositories.OrderRepository;
import org.example.ecommerce.Repositories.ProductRepository;
import org.example.ecommerce.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse create(OrderRequest request, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Produit non trouvé avec l'id " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new BadRequestException(
                        "Stock insuffisant pour le produit \"" + product.getName() +
                                "\" (disponible : " + product.getStock() + ")");
            }

            // Décrémente le stock
            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(lineTotal);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .user(user)
                .total(total)
                .status(OrderStatus.PENDING)
                .shippingAddress(request.getShippingAddress())
                .build();

        // Lie chaque item à la commande (nécessaire pour la relation bidirectionnelle)
        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);

        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getMyOrders(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<OrderResponse> getAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée avec l'id " + id));
        order.setStatus(status);
        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderResponse.OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userEmail(order.getUser().getEmail())
                .items(items)
                .total(order.getTotal())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
package com.ecommerce.order_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.order_service.dto.OrderItemDTO;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.model.CartItem;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;
import com.ecommerce.order_service.model.OrderStatus;
import com.ecommerce.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository; 
    
    @Transactional
    public Optional<OrderResponse> createOrder(String userIdStr) {
        String userId;
        try {
            userId = String.valueOf(userIdStr);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        List<CartItem> cartItems = cartService.getUserCart(userIdStr);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        // Optional<User> userOptional = userRepository.findById(userId);
        // if (userOptional.isEmpty()) {
        //     return Optional.empty();
        // }
        // User user = userOptional.get();

        // Calculate total price: unit price * quantity for each item
        BigDecimal totalPrice = cartItems.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        
        List<OrderItem> orderItems = cartItems.stream()
            .map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getPrice());
                orderItem.setOrder(order);
                return orderItem;
            })
            .toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userIdStr);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getItems().stream()
                .map(orderItem -> new OrderItemDTO(
                    orderItem.getId(),
                    orderItem.getProductId(),
                    orderItem.getQuantity(),
                    orderItem.getPrice(),
                    orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                ))
                .toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
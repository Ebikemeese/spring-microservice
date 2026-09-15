package com.ecommerce.order_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("/api/orders") 
public class OrderController {

    private final OrderService orderService;

    @PostMapping 
    public ResponseEntity<OrderResponse> createOrder(
        @RequestHeader("X-User-Id") String userId
    ) {
        return orderService.createOrder(userId)
            .map(orderResponse -> ResponseEntity.status(HttpStatus.CREATED).body(orderResponse))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
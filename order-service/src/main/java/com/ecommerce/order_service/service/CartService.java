package com.ecommerce.order_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.order_service.dto.CartItemRequest;
import com.ecommerce.order_service.model.CartItem;

import jakarta.transaction.Transactional;

import com.ecommerce.order_service.repository.CartItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional 
public class CartService {
    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        // Optional<Product> productOpt = productRepository.findById(request.getProductId());

        // if (productOpt.isEmpty()) {
        //     return false;
        // }

        // Product product = productOpt.get();
        // if (product.getStockQuantity() < request.getQuantity()) {
        //     return false;
        // }

        // Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        // if (userOpt.isEmpty()) {
        //     return false;
        // }

        // User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }

        return false;
    }

    public List<CartItem> getUserCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }
    
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}

package com.Paypilot.Paypilot_backend.service;

import com.Paypilot.Paypilot_backend.model.CartItem;
import com.Paypilot.Paypilot_backend.model.Product;
import com.Paypilot.Paypilot_backend.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final ProductRepository productRepository;

    private final List<CartItem> cart = new ArrayList<>();

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public String addToCart(Long productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        CartItem existingItem = cart.stream()
                .filter(item ->
                        item.getProduct()
                                .getId()
                                .equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {

            existingItem.setQuantity(
                    existingItem.getQuantity() + quantity
            );

        } else {

            cart.add(new CartItem(product, quantity));
        }

        return product.getName() + " added to cart.";
    }


    /*
     * Increase or decrease quantity
     *
     * change = +1 → increase
     * change = -1 → decrease
     */
    public String updateQuantity(
            Long productId,
            int change) {

        CartItem existingItem = cart.stream()
                .filter(item ->
                        item.getProduct()
                                .getId()
                                .equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem == null) {
            return "Product not found in cart.";
        }

        int newQuantity =
                existingItem.getQuantity() + change;

        /*
         * If quantity becomes 0,
         * remove the product completely.
         */
        if (newQuantity <= 0) {

            cart.remove(existingItem);

            return "Product removed from cart.";
        }

        existingItem.setQuantity(newQuantity);

        return "Cart quantity updated.";
    }


    /*
     * Remove product completely
     */
    public String removeFromCart(Long productId) {

        CartItem existingItem = cart.stream()
                .filter(item ->
                        item.getProduct()
                                .getId()
                                .equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem == null) {
            return "Product not found in cart.";
        }

        cart.remove(existingItem);

        return "Product removed from cart.";
    }


    /*
     * Calculate cart total
     */
    public double getCartTotal() {

        return cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }


    /*
     * Clear entire cart
     */
    public void clearCart() {
        cart.clear();
    }
}
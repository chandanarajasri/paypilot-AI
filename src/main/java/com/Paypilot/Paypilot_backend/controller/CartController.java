package com.Paypilot.Paypilot_backend.controller;

import com.Paypilot.Paypilot_backend.model.CartItem;
import com.Paypilot.Paypilot_backend.service.CartService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItem> getCart() {
        return cartService.getCart();
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity) {

        return cartService.addToCart(productId, quantity);
    }

    /*
     * Increase / decrease quantity
     */
    @PutMapping("/update")
    public String updateQuantity(
            @RequestParam Long productId,
            @RequestParam int change) {

        return cartService.updateQuantity(productId, change);
    }

    /*
     * Remove one product completely
     */
    @DeleteMapping("/remove")
    public String removeFromCart(
            @RequestParam Long productId) {

        return cartService.removeFromCart(productId);
    }

    @GetMapping("/total")
    public double getCartTotal() {
        return cartService.getCartTotal();
    }

    @DeleteMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "Cart cleared.";
    }
}
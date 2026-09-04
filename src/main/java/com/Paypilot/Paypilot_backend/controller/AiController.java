package com.Paypilot.Paypilot_backend.controller;

import com.Paypilot.Paypilot_backend.model.Product;
import com.Paypilot.Paypilot_backend.service.AiService;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam String message) {

        return aiService.searchProducts(message);
    }

    @GetMapping("/recommend")
    public String recommend(
            @RequestParam String message) {

        return aiService.generateRecommendation(message);
    }

    @GetMapping("/recommend-data")
public Map<String, Object> recommendData(
        @RequestParam String message,
        @RequestParam(defaultValue = "best") String mode) {

    Product product =
            aiService.getBestProduct(message,mode);

    Map<String, Object> result =
            new HashMap<>();

    if (product == null) {

        result.put("found", false);

        result.put(
                "message",
                "Sorry, I couldn't find a suitable product."
        );

        return result;
    }

    String recommendation;

if ("cheapest".equalsIgnoreCase(mode)) {
    recommendation =
            "This is the lowest-priced matching product for your requirements.";
} else if ("value".equalsIgnoreCase(mode)) {
    recommendation =
            "This product offers the best balance of features, suitability, and price.";
} else {
    recommendation =
            aiService.generateRecommendation(message);
}

    result.put("found", true);
    result.put("productId", product.getId());
    result.put("productName", product.getName());
    result.put("price", product.getPrice());
    result.put("category", product.getCategory());
    result.put("description", product.getDescription());
    result.put("imageUrl", product.getImageUrl());
    result.put("recommendation", recommendation);

    return result;
}

    @GetMapping("/upsell")
    public String upsell(
            @RequestParam Long productId) {

        return aiService.generateUpsell(productId);
    }
}
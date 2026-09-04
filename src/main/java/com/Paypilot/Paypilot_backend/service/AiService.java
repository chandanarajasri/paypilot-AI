package com.Paypilot.Paypilot_backend.service;

import com.Paypilot.Paypilot_backend.model.Product;
import com.Paypilot.Paypilot_backend.repository.ProductRepository;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiService {

    private final ProductRepository productRepository;
    private final Client geminiClient;

    public AiService(ProductRepository productRepository) {

        this.productRepository = productRepository;

        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {

            throw new IllegalStateException(
                    "GEMINI_API_KEY environment variable is not set."
            );
        }

        this.geminiClient = Client.builder()
                .apiKey(apiKey)
                .build();
    }


    // =========================================================
    // SEARCH PRODUCTS FROM DATABASE
    // =========================================================

    public List<Product> searchProducts(String message) {

        String request = message
                .toLowerCase()
                .replace("-", " ");

        List<Product> products =
                productRepository.findAll();

        Double budget =
                extractBudget(request);

        return products.stream()
                .filter(product -> {

                    String name =
                            product.getName()
                                    .toLowerCase()
                                    .replace("-", " ");

                    String category =
                            product.getCategory()
                                    .toLowerCase()
                                    .replace("-", " ");

                    String description =
                            product.getDescription()
                                    .toLowerCase()
                                    .replace("-", " ");

                    String[] words =
                            request.split("\\s+");

                    boolean keywordMatch = false;

                    for (String word : words) {

                        if (word.length() < 3
                                || word.equals("under")
                                || word.equals("below")
                                || word.equals("less")
                                || word.equals("than")
                                || word.equals("for")
                                || word.equals("need")
                                || word.equals("want")
                                || word.equals("with")) {

                            continue;
                        }

                        if (name.contains(word)
                                || category.contains(word)
                                || description.contains(word)) {

                            keywordMatch = true;
                            break;
                        }
                    }

                    boolean budgetMatch =
                            budget == null
                            || product.getPrice() <= budget;

                    return keywordMatch && budgetMatch;
                })
                .collect(Collectors.toList());
    }


    // =========================================================
    // GET BEST PRODUCT
    // =========================================================

    public Product getBestProduct(String message, String mode) {

    List<Product> products = searchProducts(message);

    if (products.isEmpty()) {
        return null;
    }

    // =========================
    // CHEAPEST
    // =========================
    if ("cheapest".equalsIgnoreCase(mode)) {

        return products.stream()
                .min((p1, p2) ->
                        Double.compare(
                                p1.getPrice(),
                                p2.getPrice()
                        ))
                .orElse(products.get(0));
    }

    // =========================
    // BEST VALUE
    // =========================
    if ("value".equalsIgnoreCase(mode)) {

        String productInformation =
                products.stream()
                        .map(product ->
                                "ID: " + product.getId()
                                + " | "
                                + product.getName()
                                + " | ₹"
                                + product.getPrice()
                                + " | "
                                + product.getCategory()
                                + " | "
                                + product.getDescription())
                        .collect(Collectors.joining("\n"));

        String prompt = """
                You are PayPilot AI's Best Value selector.

                Customer request:
                %s

                Available products:
                %s

                Choose the product that gives the BEST VALUE.

                Consider:
                - How well it matches the customer's request
                - Features and usefulness
                - Price
                - Overall balance between price and features

                IMPORTANT:
                Return ONLY the numeric product ID.
                Do not return the product name.
                Do not return an explanation.
                Do not return markdown.
                Do not return any other text.

                """.formatted(
                        message,
                        productInformation
                );

        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        "gemini-3.6-flash",
                        prompt,
                        null
                );

        String aiAnswer =
                response.text()
                        .trim()
                        .replaceAll("[^0-9]", "");

        try {

            Long selectedId =
                    Long.parseLong(aiAnswer);

            return products.stream()
                    .filter(product ->
                            product.getId().equals(selectedId))
                    .findFirst()
                    .orElse(products.get(0));

        } catch (NumberFormatException e) {

            return products.get(0);
        }
    }

    // =========================
    // BEST MATCH
    // =========================

    String productInformation =
            products.stream()
                    .map(product ->
                            "ID: " + product.getId()
                            + " | "
                            + product.getName()
                            + " | ₹"
                            + product.getPrice()
                            + " | "
                            + product.getCategory()
                            + " | "
                            + product.getDescription())
                    .collect(Collectors.joining("\n"));

    String prompt = """
            You are a product selection engine for PayPilot.

            Customer request:
            %s

            Available products:
            %s

            Choose the single BEST MATCH for the customer.

            Consider:
            - Customer requirements
            - Product category
            - Product features
            - Description
            - Budget

            IMPORTANT:
            Return ONLY the numeric product ID.
            Do not return the product name.
            Do not return any explanation.
            Do not return markdown.
            Do not return any other text.

            """.formatted(
                    message,
                    productInformation
            );

    GenerateContentResponse response =
            geminiClient.models.generateContent(
                    "gemini-3.6-flash",
                    prompt,
                    null
            );

    String aiAnswer =
            response.text()
                    .trim()
                    .replaceAll("[^0-9]", "");

    try {

        Long selectedId =
                Long.parseLong(aiAnswer);

        return products.stream()
                .filter(product ->
                        product.getId().equals(selectedId))
                .findFirst()
                .orElse(products.get(0));

    } catch (NumberFormatException e) {

        return products.get(0);
    }
}


    // =========================================================
    // GENERATE AI RECOMMENDATION
    // =========================================================

    public String generateRecommendation(String message) {

        List<Product> products =
                searchProducts(message);

        if (products.isEmpty()) {

            return "Sorry, I couldn't find a suitable product for your request.";
        }

        String productInformation =
                products.stream()
                        .map(product ->
                                "ID: " + product.getId()
                                + " | "
                                + product.getName()
                                + " | ₹"
                                + product.getPrice()
                                + " | "
                                + product.getCategory()
                                + " | "
                                + product.getDescription())
                        .collect(Collectors.joining("\n"));

        String prompt = """

                You are PayPilot AI, a helpful shopping assistant.

                Customer request:
                %s

                Available products:
                %s

                Recommend the most suitable product.

                Rules:
                - Only recommend a product from the provided list.
                - Never invent a product or price.
                - Mention the exact product name and price.
                - Explain briefly why it matches the request.
                - If an appropriate complementary product exists in the provided list, you may mention it.
                - Never recommend a product that is not in the provided list.
                - Keep the response short and friendly.
                - Do not use excessive blank lines.
                - Do not use markdown bold symbols such as **.
                - Put "Why it's a great match:" on a new line when appropriate.

                """.formatted(
                        message,
                        productInformation
                );

        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        "gemini-3.6-flash",
                        prompt,
                        null
                );

        return response.text();
    }


    // =========================================================
    // EXTRACT BUDGET
    // =========================================================

    private Double extractBudget(String message) {

        String[] words =
                message.split(" ");

        for (String word : words) {

            String cleanedWord =
                    word
                            .replace("₹", "")
                            .replace(",", "")
                            .replace("rs", "");

            try {

                double value =
                        Double.parseDouble(cleanedWord);

                if (value > 100) {
                    return value;
                }

            } catch (NumberFormatException ignored) {

            }
        }

        return null;
    }


    // =========================================================
    // AI UPSELL
    // =========================================================

    public String generateUpsell(Long productId) {

        Product selectedProduct =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );

        List<Product> products =
                productRepository.findAll();

        String productInformation =
                products.stream()
                        .filter(product ->
                                !product.getId()
                                        .equals(productId))
                        .map(product ->
                                product.getName()
                                + " - ₹"
                                + product.getPrice()
                                + " - "
                                + product.getCategory()
                                + " - "
                                + product.getDescription())
                        .collect(Collectors.joining("\n"));

        String prompt = """

                You are PayPilot AI, a helpful shopping assistant.

                Customer selected:

                %s - ₹%.2f

                Category: %s

                Other available products:

                %s

                Suggest ONE complementary product.

                Rules:
                - Only recommend a product from the provided list.
                - Do not invent products or prices.
                - Keep the response under 50 words.
                - Explain briefly why it complements the selected product.
                - Do not recommend the same product.
                - Do not use markdown bold symbols such as **.

                """.formatted(
                        selectedProduct.getName(),
                        selectedProduct.getPrice(),
                        selectedProduct.getCategory(),
                        productInformation
                );

        GenerateContentResponse response =
                geminiClient.models.generateContent(
                        "gemini-3.6-flash",
                        prompt,
                        null
                );

        return response.text();
    }
}
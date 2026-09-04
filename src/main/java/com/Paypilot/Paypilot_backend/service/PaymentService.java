package com.Paypilot.Paypilot_backend.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.Paypilot.Paypilot_backend.model.AuditLog;
import com.Paypilot.Paypilot_backend.repository.AuditLogRepository;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final AuditLogRepository auditLogRepository;

    public PaymentService(
            AuditLogRepository auditLogRepository) throws Exception {

        String keyId = System.getenv("RAZORPAY_KEY_ID");
        String keySecret = System.getenv("RAZORPAY_KEY_SECRET");

        if (keyId == null || keyId.isBlank()) {
            throw new IllegalStateException(
                    "RAZORPAY_KEY_ID environment variable is not set."
            );
        }

        if (keySecret == null || keySecret.isBlank()) {
            throw new IllegalStateException(
                    "RAZORPAY_KEY_SECRET environment variable is not set."
            );
        }

        this.razorpayClient =
                new RazorpayClient(keyId, keySecret);

        this.auditLogRepository = auditLogRepository;
    }

    public String createOrder(double amount) throws Exception {

        int amountInPaise =
                (int) Math.round(amount * 100);

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");

        orderRequest.put(
                "receipt",
                "paypilot_" + System.currentTimeMillis()
        );

        Order order =
                razorpayClient.orders.create(orderRequest);

        AuditLog auditLog = new AuditLog(
                "CREATE_PAYMENT_ORDER",
                "Cart",
                amount,
                order.get("id").toString(),
                null,
                "CREATED"
        );

        auditLogRepository.save(auditLog);

        return order.toString();
    }

    public boolean verifyPayment(
        String orderId,
        String paymentId,
        String signature) throws Exception {

    String keySecret =
            System.getenv("RAZORPAY_KEY_SECRET");

    // Get the original order from Razorpay
    Order order =
            razorpayClient.orders.fetch(orderId);

    // Razorpay stores amount in paise
    double amount =
        ((Number) order.get("amount")).doubleValue() / 100.0;
    String data =
            orderId + "|" + paymentId;

    Mac mac =
            Mac.getInstance("HmacSHA256");

    SecretKeySpec secretKeySpec =
            new SecretKeySpec(
                    keySecret.getBytes(
                            StandardCharsets.UTF_8
                    ),
                    "HmacSHA256"
            );

    mac.init(secretKeySpec);

    byte[] hash =
            mac.doFinal(
                    data.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

    StringBuilder generatedSignature =
            new StringBuilder();

    for (byte b : hash) {

        generatedSignature.append(
                String.format("%02x", b)
        );
    }

    boolean verified =
            generatedSignature
                    .toString()
                    .equals(signature);

    AuditLog auditLog =
            new AuditLog(
                    "PAYMENT_VERIFICATION",
                    "Cart",
                    amount,
                    orderId,
                    paymentId,
                    verified
                            ? "VERIFIED"
                            : "FAILED"
            );

    auditLogRepository.save(auditLog);

    return verified;
}
}
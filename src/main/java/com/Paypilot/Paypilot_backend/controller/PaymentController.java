package com.Paypilot.Paypilot_backend.controller;

import com.Paypilot.Paypilot_backend.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import com.Paypilot.Paypilot_backend.model.AuditLog;
import com.Paypilot.Paypilot_backend.repository.AuditLogRepository;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;
    private final AuditLogRepository auditLogRepository;

    public PaymentController(PaymentService paymentService,AuditLogRepository auditLogRepository) {
        this.paymentService = paymentService;
        this.auditLogRepository = auditLogRepository;
    }
@GetMapping("/audit")
public List<AuditLog> getAuditLogs() {
    return auditLogRepository.findAll();
}
    @PostMapping("/create-order")
    public String createOrder(@RequestParam double amount)
            throws Exception {

        return paymentService.createOrder(amount);
    }
    @PostMapping("/verify")
public String verifyPayment(
        @RequestParam String orderId,
        @RequestParam String paymentId,
        @RequestParam String signature) {

    try {

        boolean verified =
                paymentService.verifyPayment(
                        orderId,
                        paymentId,
                        signature
                );

        if (verified) {
            return "PAYMENT_VERIFIED";
        }

        return "PAYMENT_VERIFICATION_FAILED: Invalid payment signature";

    } catch (Exception e) {

        return "PAYMENT_VERIFICATION_FAILED: Payment could not be verified";

    }
}
}
package com.Paypilot.Paypilot_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private String productName;

    private Double amount;

    private String orderId;

    private String paymentId;

    private String status;

    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(
            String action,
            String productName,
            Double amount,
            String orderId,
            String paymentId,
            String status) {

        this.action = action;
        this.productName = productName;
        this.amount = amount;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getProductName() {
        return productName;
    }

    public Double getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
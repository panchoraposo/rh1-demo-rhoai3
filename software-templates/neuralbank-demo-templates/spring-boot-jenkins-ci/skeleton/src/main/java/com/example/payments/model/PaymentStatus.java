package com.example.payments.model;

import java.time.Instant;

public record PaymentStatus(
    String id,
    String state,
    Instant updatedAt,
    String decision,
    String riskLevel
) {}


package com.example.payments.model;

import java.time.Instant;
import java.util.Map;

public record PaymentEvent(
    String type,
    String paymentId,
    Instant at,
    Map<String, Object> data
) {}


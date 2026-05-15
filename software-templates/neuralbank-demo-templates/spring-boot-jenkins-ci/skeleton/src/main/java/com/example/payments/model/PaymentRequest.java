package com.example.payments.model;

import java.math.BigDecimal;

public record PaymentRequest(
    String customerId,
    String currency,
    BigDecimal amount,
    String reference
) {}


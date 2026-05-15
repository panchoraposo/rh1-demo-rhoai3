package com.example.payments.model;

public record PaymentAccepted(
    String id,
    String status,
    String statusUrl,
    String eventsUrl
) {}


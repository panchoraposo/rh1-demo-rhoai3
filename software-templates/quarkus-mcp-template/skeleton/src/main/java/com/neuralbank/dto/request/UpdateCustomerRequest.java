package com.neuralbank.dto.request;

public record UpdateCustomerRequest(
    String firstName,
    String lastName,
    String email,
    Boolean active
) {}


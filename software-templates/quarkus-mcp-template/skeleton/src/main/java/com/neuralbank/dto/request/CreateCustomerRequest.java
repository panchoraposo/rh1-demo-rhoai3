package com.neuralbank.dto.request;

import com.neuralbank.enums.CustomerType;

public record CreateCustomerRequest(
    CustomerType type,
    String identification,
    String firstName,
    String lastName,
    String email
) {}


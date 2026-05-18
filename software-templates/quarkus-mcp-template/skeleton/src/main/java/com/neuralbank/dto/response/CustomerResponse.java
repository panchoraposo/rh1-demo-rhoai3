package com.neuralbank.dto.response;

import com.neuralbank.enums.CustomerType;

public record CustomerResponse(
    Long id,
    CustomerType type,
    String identification,
    String firstName,
    String lastName,
    String email,
    Boolean active
) {}


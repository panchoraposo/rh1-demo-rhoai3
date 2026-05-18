package com.neuralbank.dto.response;

import java.math.BigDecimal;

public record CreditScoreResponse(
    Long customerId,
    BigDecimal score,
    String riskLevel
) {}


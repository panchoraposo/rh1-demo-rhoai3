package com.neuralbank.dto.response;

import java.math.BigDecimal;

public record CustomerSummaryResponse(
    CustomerResponse customer,
    CreditScoreResponse creditScore,
    BigDecimal totalBalance,
    int accounts
) {}


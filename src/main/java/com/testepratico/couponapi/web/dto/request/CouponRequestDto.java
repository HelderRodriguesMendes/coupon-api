package com.testepratico.couponapi.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CouponRequestDto(

    @NotBlank(message = "O código é obrigatório")
    String code,

    @NotBlank(message = "A descrição é obrigatória")
    String description,

    @NotNull(message = "O valor do desconto é obrigatório")
    BigDecimal discountValue,

    @NotNull(message = "A data de expiração é obrigatória")
    Instant expirationDate,

    boolean published
) {}
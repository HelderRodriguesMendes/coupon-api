package com.testepratico.couponapi.web.dto.response;

import com.testepratico.couponapi.domain.enums.CouponStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CouponResponseDto(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    Instant expirationDate,
    boolean published,
    CouponStatus status,
    boolean redeemed) {
}
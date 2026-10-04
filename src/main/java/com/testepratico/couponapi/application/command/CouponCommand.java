package com.testepratico.couponapi.application.command;

import java.math.BigDecimal;
import java.time.Instant;

public record CouponCommand (
    String code,
    String description,
    BigDecimal discountValue,
    Instant expirationDate,
    boolean published){}
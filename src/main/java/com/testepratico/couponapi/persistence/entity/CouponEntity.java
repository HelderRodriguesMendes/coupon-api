package com.testepratico.couponapi.persistence.entity;

import com.testepratico.couponapi.domain.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "coupon")
public class CouponEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private Instant expirationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;
}
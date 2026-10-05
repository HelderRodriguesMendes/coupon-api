package com.testepratico.couponapi.application.port;

import com.testepratico.couponapi.domain.model.CouponModel;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepositoryPort {
    CouponModel save(CouponModel couponModel);
    Optional<CouponModel> findById(UUID id);
}
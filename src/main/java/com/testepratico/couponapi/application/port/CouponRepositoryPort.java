package com.testepratico.couponapi.application.port;

import com.testepratico.couponapi.domain.model.CouponModel;

public interface CouponRepositoryPort {
    CouponModel save(CouponModel couponModel);
}
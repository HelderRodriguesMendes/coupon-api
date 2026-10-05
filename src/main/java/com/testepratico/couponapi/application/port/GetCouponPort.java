package com.testepratico.couponapi.application.port;

import com.testepratico.couponapi.domain.model.CouponModel;

import java.util.UUID;

public interface GetCouponPort {

    CouponModel getCoupon(UUID id);
}
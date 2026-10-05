package com.testepratico.couponapi.application.port;

import java.util.UUID;

public interface DeleteCouponPort {
    void deleteCoupon(UUID id);
}
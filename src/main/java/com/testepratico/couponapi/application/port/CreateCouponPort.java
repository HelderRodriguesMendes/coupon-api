package com.testepratico.couponapi.application.port;

import com.testepratico.couponapi.application.command.CouponCommand;
import com.testepratico.couponapi.domain.model.CouponModel;

public interface CreateCouponPort {
    CouponModel createCoupon(CouponCommand couponCommand);
}
package com.testepratico.couponapi.application.usecase;

import com.testepratico.couponapi.application.command.CouponCommand;
import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.application.port.CreateCouponPort;
import com.testepratico.couponapi.domain.model.CouponModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCouponUseCase implements CreateCouponPort {

    private final CouponRepositoryPort couponRepositoryPort;

    @Override
    public CouponModel createCoupon(CouponCommand couponCommand) {
        CouponModel coupon = CouponModel.create(
                couponCommand.code(),
                couponCommand.description(),
                couponCommand.discountValue(),
                couponCommand.expirationDate(),
                couponCommand.published()
        );
        return couponRepositoryPort.save(coupon);
    }
}
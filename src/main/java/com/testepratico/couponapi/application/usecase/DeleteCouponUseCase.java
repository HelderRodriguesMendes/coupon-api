package com.testepratico.couponapi.application.usecase;

import com.testepratico.couponapi.application.port.CouponRepositoryPort;
import com.testepratico.couponapi.application.port.DeleteCouponPort;
import com.testepratico.couponapi.domain.enums.MessageException;
import com.testepratico.couponapi.domain.exception.CouponNotFoundException;
import com.testepratico.couponapi.domain.model.CouponModel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteCouponUseCase implements DeleteCouponPort {

    private final CouponRepositoryPort couponRepositoryPort;

    @Override
    public void deleteCoupon(UUID id) {
        CouponModel couponModel = couponRepositoryPort.findById(id)
            .orElseThrow(() ->
                new CouponNotFoundException(
                    MessageException.COUPON_NOT_FOUND.getMessage()
                )
            );
        couponModel.delete();
        couponRepositoryPort.save(couponModel);
    }
}